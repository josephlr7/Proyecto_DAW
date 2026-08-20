package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.EscuelaDTO;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.api.exception.SolicitudInvalidaException;
import com.example.demo.business.data.entity.Escuela;
import com.example.demo.business.data.entity.Facultad;
import com.example.demo.business.data.repository.EscuelaRepository;
import com.example.demo.business.data.repository.FacultadRepository;
import com.example.demo.business.data.repository.LaboratorioRepository;
import com.example.demo.business.domain.service.EscuelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EscuelaServiceImpl implements EscuelaService {

    private final EscuelaRepository escuelaRepository;
    private final FacultadRepository facultadRepository;
    private final LaboratorioRepository laboratorioRepository;

    @Override
    @Transactional
    public EscuelaDTO registrarEscuela(EscuelaDTO dto) {
        Facultad facultad = facultadRepository.findById(dto.facultadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + dto.facultadId()));
        
        if (escuelaRepository.findByNombreIgnoreCaseAndFacultad(dto.nombre(), facultad).isPresent()) {
            throw new SolicitudInvalidaException("Ya existe una escuela con el nombre '" + dto.nombre() + "' en la facultad " + facultad.getNombre());
        }

        Escuela escuela = new Escuela(dto.nombre(), facultad);
        Escuela guardada = escuelaRepository.save(escuela);
        return toDto(guardada);
    }

    @Override
    @Transactional
    public EscuelaDTO actualizarEscuela(Long id, EscuelaDTO dto) {
        Escuela escuela = escuelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Escuela no encontrada con id: " + id));

        Facultad facultad = facultadRepository.findById(dto.facultadId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + dto.facultadId()));

        escuelaRepository.findByNombreIgnoreCaseAndFacultad(dto.nombre(), facultad)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new SolicitudInvalidaException("Ya existe otra escuela con el nombre '" + dto.nombre() + "' en la facultad " + facultad.getNombre());
                    }
                });

        escuela.setNombre(dto.nombre());
        escuela.setFacultad(facultad);
        Escuela guardada = escuelaRepository.save(escuela);
        return toDto(guardada);
    }

    @Override
    public EscuelaDTO obtenerPorId(Long id) {
        Escuela escuela = escuelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Escuela no encontrada con id: " + id));
        return toDto(escuela);
    }

    @Override
    public List<EscuelaDTO> obtenerTodas() {
        return escuelaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EscuelaDTO> obtenerPorFacultad(Long facultadId) {
        return escuelaRepository.findByFacultadId(facultadId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarEscuela(Long id) {
        Escuela escuela = escuelaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Escuela no encontrada con id: " + id));

        if (laboratorioRepository.existsByEscuelaId(id)) {
            throw new SolicitudInvalidaException("No se puede eliminar la escuela porque está asociada a laboratorios activos");
        }

        escuelaRepository.delete(escuela);
    }

    private EscuelaDTO toDto(Escuela escuela) {
        return new EscuelaDTO(escuela.getId(), escuela.getNombre(), escuela.getFacultad().getId());
    }
}
