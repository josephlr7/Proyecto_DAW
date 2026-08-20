package com.example.demo.business.domain.service.impl;

import com.example.demo.business.data.entity.Facultad;
import com.example.demo.business.data.entity.Escuela;
import com.example.demo.business.data.repository.FacultadRepository;
import com.example.demo.business.data.repository.EscuelaRepository;
import com.example.demo.business.api.dto.LaboratorioDTO;
import com.example.demo.business.data.entity.Laboratorio;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.data.repository.LaboratorioRepository;
import com.example.demo.business.domain.service.LaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LaboratorioServiceImpl implements LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;
    private final FacultadRepository facultadRepository;
    private final EscuelaRepository escuelaRepository;

    @Override
    @Transactional
    public LaboratorioDTO registrarLaboratorio(LaboratorioDTO dto) {
        Laboratorio laboratorio = toEntity(dto);
        Laboratorio guardado = laboratorioRepository.save(laboratorio);
        return toDto(guardado);
    }

    @Override
    public LaboratorioDTO obtenerPorId(Long id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + id));
        return toDto(laboratorio);
    }

    @Override
    public List<LaboratorioDTO> obtenerTodos() {
        return laboratorioRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<LaboratorioDTO> consultar(String escuela, String facultad, org.springframework.data.domain.Pageable pageable) {
        String escuelaNorm = (escuela == null || escuela.isBlank()) ? null : escuela.trim();
        String facultadNorm = (facultad == null || facultad.isBlank()) ? null : facultad.trim();
        return laboratorioRepository.buscarLaboratorios(escuelaNorm, facultadNorm, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional
    public LaboratorioDTO actualizarLaboratorio(Long id, LaboratorioDTO dto) {
        Laboratorio entity = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + id));

        Facultad fac = facultadRepository.findByNombreIgnoreCase(dto.facultad())
                .orElseGet(() -> facultadRepository.save(new Facultad(dto.facultad())));

        Escuela esc = escuelaRepository.findByNombreIgnoreCaseAndFacultad(dto.escuela(), fac)
                .orElseGet(() -> escuelaRepository.save(new Escuela(dto.escuela(), fac)));

        entity.setFacultad(fac);
        entity.setEscuela(esc);
        entity.setPoseeSistemaGestion(dto.poseeSistemaGestion());

        Laboratorio guardado = laboratorioRepository.save(entity);
        return toDto(guardado);
    }

    private Laboratorio toEntity(LaboratorioDTO dto) {
        Laboratorio entity = new Laboratorio();
        
        Facultad fac = facultadRepository.findByNombreIgnoreCase(dto.facultad())
                .orElseGet(() -> facultadRepository.save(new Facultad(dto.facultad())));

        Escuela esc = escuelaRepository.findByNombreIgnoreCaseAndFacultad(dto.escuela(), fac)
                .orElseGet(() -> escuelaRepository.save(new Escuela(dto.escuela(), fac)));

        entity.setFacultad(fac);
        entity.setEscuela(esc);
        entity.setPoseeSistemaGestion(dto.poseeSistemaGestion());
        return entity;
    }

    private LaboratorioDTO toDto(Laboratorio entity) {
        return new LaboratorioDTO(
                entity.getId(),
                entity.getFacultad().getNombre(),
                entity.getEscuela().getNombre(),
                entity.getPoseeSistemaGestion()
        );
    }

    @Override
    @Transactional
    public void eliminarLaboratorio(Long id) {
        Laboratorio lab = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + id));

        boolean tieneRelaciones = !lab.getPersonal().isEmpty() || !lab.getEquipamientos().isEmpty() || !lab.getConsumibles().isEmpty();

        if (tieneRelaciones) {
            lab.setActivo(false);
            laboratorioRepository.save(lab);
        } else {
            laboratorioRepository.delete(lab);
        }
    }
}

