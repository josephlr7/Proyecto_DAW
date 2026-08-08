package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.LaboratorioDTO;
import java.util.List;

public interface LaboratorioService {
    LaboratorioDTO registrarLaboratorio(LaboratorioDTO dto);
    LaboratorioDTO obtenerPorId(Long id);
    List<LaboratorioDTO> obtenerTodos();
    org.springframework.data.domain.Page<LaboratorioDTO> consultar(String escuela, String facultad, org.springframework.data.domain.Pageable pageable);
    void eliminarLaboratorio(Long id);
}

