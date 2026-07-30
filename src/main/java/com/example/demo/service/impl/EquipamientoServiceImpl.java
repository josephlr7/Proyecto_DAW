package com.example.demo.service.impl;

import com.example.demo.dto.EquipamientoDTO;
import com.example.demo.dto.EquipamientoSemaforoDTO;
import com.example.demo.entity.Equipamiento;
import com.example.demo.entity.Laboratorio;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EquipamientoRepository;
import com.example.demo.repository.LaboratorioRepository;
import com.example.demo.service.EquipamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipamientoServiceImpl implements EquipamientoService {

    private final EquipamientoRepository equipamientoRepository;
    private final LaboratorioRepository laboratorioRepository;

    @Override
    @Transactional
    public EquipamientoDTO registrarEquipamiento(EquipamientoDTO dto) {
        Laboratorio lab = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con id: " + dto.laboratorioId()));

        Equipamiento e = toEntity(dto);
        e.setLaboratorio(lab);
        Equipamiento guardado = equipamientoRepository.save(e);
        return toDto(guardado);
    }

    @Override
    @Transactional
    public EquipamientoDTO actualizarEquipamiento(Long id, EquipamientoDTO dto) {
        Equipamiento e = equipamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamiento no encontrado con id: " + id));

        Laboratorio lab = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(() -> new ResourceNotFoundException("Laboratorio no encontrado con id: " + dto.laboratorioId()));

        e.setNombre(dto.nombre());
        e.setFuncion(dto.funcion());
        e.setRangoPrecio(dto.rangoPrecio());
        e.setAnoAdquisicion(dto.anoAdquisicion());
        e.setHorasUso(dto.horasUso());
        e.setEstado(dto.estado());
        e.setProgramaMantenimiento(dto.programaMantenimiento());
        e.setProgramaMantenimientoHoras(dto.programaMantenimientoHoras());
        e.setSeCumpleMantenimiento(dto.seCumpleMantenimiento());
        e.setRequiereConsumible(dto.requiereConsumible());
        e.setTipoConsumibleRequerido(dto.tipoConsumibleRequerido());
        e.setLaboratorio(lab);

        Equipamiento guardado = equipamientoRepository.save(e);
        return toDto(guardado);
    }

    @Override
    public EquipamientoDTO obtenerPorId(Long id) {
        Equipamiento e = equipamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamiento no encontrado con id: " + id));
        return toDto(e);
    }

    @Override
    public List<EquipamientoDTO> obtenerTodos() {
        return equipamientoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipamientoDTO> obtenerPorLaboratorioId(Long laboratorioId) {
        return equipamientoRepository.buscarPorLaboratorioId(laboratorioId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipamientoSemaforoDTO> obtenerReporteSemaforo(Long laboratorioId) {
        return equipamientoRepository.buscarPorLaboratorioId(laboratorioId).stream()
                .map(e -> {
                    String semaforo = "VERDE";
                    if (e.getProgramaMantenimientoHoras() != null && e.getProgramaMantenimientoHoras() > 0) {
                        double restantes = e.getProgramaMantenimientoHoras() - e.getHorasUso();
                        if (restantes <= 100) {
                            semaforo = "ROJO";
                        } else if (restantes <= 300) {
                            semaforo = "AMARILLO";
                        }
                    }
                    return new EquipamientoSemaforoDTO(
                            e.getId(),
                            e.getNombre(),
                            e.getHorasUso(),
                            e.getProgramaMantenimientoHoras(),
                            semaforo
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarEquipamiento(Long id) {
        Equipamiento e = equipamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamiento no encontrado con id: " + id));
        equipamientoRepository.delete(e);
    }

    private Equipamiento toEntity(EquipamientoDTO dto) {
        Equipamiento e = new Equipamiento();
        e.setNombre(dto.nombre());
        e.setFuncion(dto.funcion());
        e.setRangoPrecio(dto.rangoPrecio());
        e.setAnoAdquisicion(dto.anoAdquisicion());
        e.setHorasUso(dto.horasUso());
        e.setEstado(dto.estado());
        e.setProgramaMantenimiento(dto.programaMantenimiento());
        e.setProgramaMantenimientoHoras(dto.programaMantenimientoHoras());
        e.setSeCumpleMantenimiento(dto.seCumpleMantenimiento());
        e.setRequiereConsumible(dto.requiereConsumible());
        e.setTipoConsumibleRequerido(dto.tipoConsumibleRequerido());
        return e;
    }

    private EquipamientoDTO toDto(Equipamiento e) {
        return new EquipamientoDTO(
                e.getId(),
                e.getNombre(),
                e.getFuncion(),
                e.getRangoPrecio(),
                e.getAnoAdquisicion(),
                e.getHorasUso(),
                e.getEstado(),
                e.getProgramaMantenimiento(),
                e.getProgramaMantenimientoHoras(),
                e.getSeCumpleMantenimiento(),
                e.getRequiereConsumible(),
                e.getTipoConsumibleRequerido(),
                e.getLaboratorio().getId()
        );
    }
}
