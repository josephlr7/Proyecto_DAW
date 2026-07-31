package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.LaboratorioDTO;
import java.util.List;

public interface LaboratorioService {
    LaboratorioDTO registrarLaboratorio(LaboratorioDTO dto);
    LaboratorioDTO obtenerPorId(Long id);
    List<LaboratorioDTO> obtenerTodos();
}

