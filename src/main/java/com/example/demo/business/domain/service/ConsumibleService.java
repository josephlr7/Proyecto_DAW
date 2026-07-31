package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.ConsumibleDTO;
import com.example.demo.business.api.dto.ConsumibleSemaforoDTO;
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

