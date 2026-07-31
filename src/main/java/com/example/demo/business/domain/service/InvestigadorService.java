package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.InvestigadorDTO;
import java.util.List;

public interface InvestigadorService {
    InvestigadorDTO registrarInvestigador(InvestigadorDTO dto);
    InvestigadorDTO obtenerPorId(Long id);
    List<InvestigadorDTO> obtenerTodos();
}

