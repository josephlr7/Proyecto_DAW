package com.example.demo.service;

import com.example.demo.dto.LaboratorioDTO;
import java.util.List;

public interface LaboratorioService {
    LaboratorioDTO registrarLaboratorio(LaboratorioDTO dto);
    LaboratorioDTO obtenerPorId(Long id);
    List<LaboratorioDTO> obtenerTodos();
}
