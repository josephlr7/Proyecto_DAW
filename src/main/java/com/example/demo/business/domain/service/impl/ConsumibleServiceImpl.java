package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.ConsumibleDTO;
import com.example.demo.business.api.dto.ConsumibleSemaforoDTO;
import com.example.demo.business.data.entity.Consumible;
import com.example.demo.business.data.entity.Laboratorio;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.data.repository.ConsumibleRepository;
import com.example.demo.business.data.repository.LaboratorioRepository;
import com.example.demo.business.domain.service.ConsumibleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumibleServiceImpl implements ConsumibleService {

    private final ConsumibleRepository consumibleRepository;
    private final LaboratorioRepository laboratorioRepository;

    @Override
    @Transactional
    public ConsumibleDTO registrarConsumible(ConsumibleDTO dto) {
        Laboratorio lab = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + dto.laboratorioId()));

        Consumible c = toEntity(dto);
        c.setLaboratorio(lab);
        Consumible guardado = consumibleRepository.save(c);
        return toDto(guardado);
    }

    @Override
    @Transactional
    public ConsumibleDTO actualizarConsumible(Long id, ConsumibleDTO dto) {
        Consumible c = consumibleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consumible no encontrado con id: " + id));

        Laboratorio lab = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + dto.laboratorioId()));

        c.setNombre(dto.nombre());
        c.setMarca(dto.marca());
        c.setEmpresa(dto.empresa());
        c.setEstadoAdquirido(dto.estadoAdquirido());
        c.setTipo(dto.tipo());
        c.setUnidadMedida(dto.unidadMedida());
        c.setFuncion(dto.funcion());
        c.setRangoPrecio(dto.rangoPrecio());
        c.setFechaAdquisicion(dto.fechaAdquisicion());
        c.setFechaVencimiento(dto.fechaVencimiento());
        c.setCantidad(dto.cantidad());
        c.setStockMinimo(dto.stockMinimo());
        c.setLaboratorio(lab);

        Consumible guardado = consumibleRepository.save(c);
        return toDto(guardado);
    }

    @Override
    public ConsumibleDTO obtenerPorId(Long id) {
        Consumible c = consumibleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consumible no encontrado con id: " + id));
        return toDto(c);
    }

    @Override
    public List<ConsumibleDTO> obtenerTodos() {
        return consumibleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsumibleDTO> obtenerPorLaboratorioId(Long laboratorioId) {
        return consumibleRepository.buscarPorLaboratorioId(laboratorioId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsumibleSemaforoDTO> obtenerReporteSemaforo(Long laboratorioId) {
        return consumibleRepository.buscarPorLaboratorioId(laboratorioId).stream()
                .map(c -> {
                    String semaforo = "VERDE";
                    if (c.getCantidad() <= c.getStockMinimo()) {
                        semaforo = "ROJO";
                    } else if (c.getCantidad() <= c.getStockMinimo() * 1.5) {
                        semaforo = "AMARILLO";
                    }
                    return new ConsumibleSemaforoDTO(
                            c.getId(),
                            c.getNombre(),
                            c.getCantidad(),
                            c.getStockMinimo(),
                            semaforo
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarConsumible(Long id) {
        Consumible c = consumibleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Consumible no encontrado con id: " + id));
        
        if (!c.getUsos().isEmpty()) {
            c.setActivo(false);
            consumibleRepository.save(c);
        } else {
            consumibleRepository.delete(c);
        }
    }

    private Consumible toEntity(ConsumibleDTO dto) {
        Consumible c = new Consumible();
        c.setNombre(dto.nombre());
        c.setMarca(dto.marca());
        c.setEmpresa(dto.empresa());
        c.setEstadoAdquirido(dto.estadoAdquirido());
        c.setTipo(dto.tipo());
        c.setUnidadMedida(dto.unidadMedida());
        c.setFuncion(dto.funcion());
        c.setRangoPrecio(dto.rangoPrecio());
        c.setFechaAdquisicion(dto.fechaAdquisicion());
        c.setFechaVencimiento(dto.fechaVencimiento());
        c.setCantidad(dto.cantidad());
        c.setStockMinimo(dto.stockMinimo());
        return c;
    }

    private ConsumibleDTO toDto(Consumible c) {
        return new ConsumibleDTO(
                c.getId(),
                c.getNombre(),
                c.getMarca(),
                c.getEmpresa(),
                c.getEstadoAdquirido(),
                c.getTipo(),
                c.getUnidadMedida(),
                c.getFuncion(),
                c.getRangoPrecio(),
                c.getFechaAdquisicion(),
                c.getFechaVencimiento(),
                c.getCantidad(),
                c.getStockMinimo(),
                c.getLaboratorio().getId()
        );
    }
}

