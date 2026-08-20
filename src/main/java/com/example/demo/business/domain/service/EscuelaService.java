package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.EscuelaDTO;
import java.util.List;

public interface EscuelaService {
    EscuelaDTO registrarEscuela(EscuelaDTO dto);
    EscuelaDTO actualizarEscuela(Long id, EscuelaDTO dto);
    EscuelaDTO obtenerPorId(Long id);
    List<EscuelaDTO> obtenerTodas();
    List<EscuelaDTO> obtenerPorFacultad(Long facultadId);
    void eliminarEscuela(Long id);
}
