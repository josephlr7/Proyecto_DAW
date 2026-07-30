package com.example.demo.service;

import com.example.demo.dto.EquipamientoDTO;
import com.example.demo.dto.EquipamientoSemaforoDTO;
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
