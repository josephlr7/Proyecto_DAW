package com.example.demo.business.domain.service.impl;

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

    private Laboratorio toEntity(LaboratorioDTO dto) {
        Laboratorio entity = new Laboratorio();
        entity.setFacultad(dto.facultad());
        entity.setEscuela(dto.escuela());
        entity.setAreaInvestigacion(dto.areaInvestigacion());
        entity.setLineasInvestigacion(dto.lineasInvestigacion());
        entity.setCategoria(dto.categoria());
        entity.setResolucionNumero(dto.resolucionNumero());
        entity.setCorreoInstitucional(dto.correoInstitucional());
        entity.setOds(dto.ods());
        entity.setPoseeSistemaGestion(dto.poseeSistemaGestion());
        return entity;
    }

    private LaboratorioDTO toDto(Laboratorio entity) {
        return new LaboratorioDTO(
                entity.getId(),
                entity.getFacultad(),
                entity.getEscuela(),
                entity.getAreaInvestigacion(),
                entity.getLineasInvestigacion(),
                entity.getCategoria(),
                entity.getResolucionNumero(),
                entity.getCorreoInstitucional(),
                entity.getOds(),
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

