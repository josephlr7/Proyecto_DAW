package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.FacultadDTO;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.api.exception.SolicitudInvalidaException;
import com.example.demo.business.data.entity.Facultad;
import com.example.demo.business.data.repository.FacultadRepository;
import com.example.demo.business.domain.service.FacultadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultadServiceImpl implements FacultadService {

    private final FacultadRepository facultadRepository;

    @Override
    @Transactional
    public FacultadDTO registrarFacultad(FacultadDTO dto) {
        if (facultadRepository.findByNombreIgnoreCase(dto.nombre()).isPresent()) {
            throw new SolicitudInvalidaException("Ya existe una facultad con el nombre: " + dto.nombre());
        }
        Facultad facultad = new Facultad(dto.nombre());
        Facultad guardada = facultadRepository.save(facultad);
        return toDto(guardada);
    }

    @Override
    @Transactional
    public FacultadDTO actualizarFacultad(Long id, FacultadDTO dto) {
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));
        
        facultadRepository.findByNombreIgnoreCase(dto.nombre())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new SolicitudInvalidaException("Ya existe otra facultad con el nombre: " + dto.nombre());
                    }
                });

        facultad.setNombre(dto.nombre());
        Facultad guardada = facultadRepository.save(facultad);
        return toDto(guardada);
    }

    @Override
    public FacultadDTO obtenerPorId(Long id) {
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));
        return toDto(facultad);
    }

    @Override
    public List<FacultadDTO> obtenerTodos() {
        return facultadRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarFacultad(Long id) {
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Facultad no encontrada con id: " + id));
        if (!facultad.getEscuelas().isEmpty()) {
            throw new SolicitudInvalidaException("No se puede eliminar la facultad porque tiene escuelas asociadas");
        }
        facultadRepository.delete(facultad);
    }

    private FacultadDTO toDto(Facultad facultad) {
        return new FacultadDTO(facultad.getId(), facultad.getNombre());
    }
}
