package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.UsoConsumibleRequestDTO;
import com.example.demo.business.api.dto.UsoConsumibleResponseDTO;
import com.example.demo.business.api.dto.UsoEquipamientoRequestDTO;
import com.example.demo.business.api.dto.UsoEquipamientoResponseDTO;

public interface UsoService {
    UsoEquipamientoResponseDTO registrarUsoEquipamiento(UsoEquipamientoRequestDTO request);
    UsoConsumibleResponseDTO registrarUsoConsumible(UsoConsumibleRequestDTO request);
    java.util.List<UsoEquipamientoResponseDTO> obtenerUsosEquipamiento();
    java.util.List<UsoConsumibleResponseDTO> obtenerUsosConsumible();
    UsoEquipamientoResponseDTO editarUsoEquipamiento(Long id, UsoEquipamientoRequestDTO request);
    UsoConsumibleResponseDTO editarUsoConsumible(Long id, UsoConsumibleRequestDTO request);
    void eliminarUsoEquipamiento(Long id);
    void eliminarUsoConsumible(Long id);
}

