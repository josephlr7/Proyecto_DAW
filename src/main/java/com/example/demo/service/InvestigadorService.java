package com.example.demo.service;

import com.example.demo.dto.InvestigadorDTO;
import java.util.List;

public interface InvestigadorService {
    InvestigadorDTO registrarInvestigador(InvestigadorDTO dto);
    InvestigadorDTO obtenerPorId(Long id);
    List<InvestigadorDTO> obtenerTodos();
}
