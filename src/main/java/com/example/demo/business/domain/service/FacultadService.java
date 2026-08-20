package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.FacultadDTO;
import java.util.List;

public interface FacultadService {
    FacultadDTO registrarFacultad(FacultadDTO dto);
    FacultadDTO actualizarFacultad(Long id, FacultadDTO dto);
    FacultadDTO obtenerPorId(Long id);
    List<FacultadDTO> obtenerTodos();
    void eliminarFacultad(Long id);
}
