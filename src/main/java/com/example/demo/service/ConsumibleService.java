package com.example.demo.service;

import com.example.demo.dto.ConsumibleDTO;
import com.example.demo.dto.ConsumibleSemaforoDTO;
import java.util.List;

public interface ConsumibleService {
    ConsumibleDTO registrarConsumible(ConsumibleDTO dto);
    ConsumibleDTO actualizarConsumible(Long id, ConsumibleDTO dto);
    ConsumibleDTO obtenerPorId(Long id);
    List<ConsumibleDTO> obtenerTodos();
    List<ConsumibleDTO> obtenerPorLaboratorioId(Long laboratorioId);
    List<ConsumibleSemaforoDTO> obtenerReporteSemaforo(Long laboratorioId);
    void eliminarConsumible(Long id);
}
