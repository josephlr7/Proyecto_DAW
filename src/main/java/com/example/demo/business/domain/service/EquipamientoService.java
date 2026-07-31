package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.EquipamientoDTO;
import com.example.demo.business.api.dto.EquipamientoSemaforoDTO;
import java.util.List;

public interface EquipamientoService {
    EquipamientoDTO registrarEquipamiento(EquipamientoDTO dto);
    EquipamientoDTO actualizarEquipamiento(Long id, EquipamientoDTO dto);
    EquipamientoDTO obtenerPorId(Long id);
    List<EquipamientoDTO> obtenerTodos();
    List<EquipamientoDTO> obtenerPorLaboratorioId(Long laboratorioId);
    List<EquipamientoSemaforoDTO> obtenerReporteSemaforo(Long laboratorioId);
    void eliminarEquipamiento(Long id);
}

