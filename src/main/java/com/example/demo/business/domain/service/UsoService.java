package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.UsoConsumibleRequestDTO;
import com.example.demo.business.api.dto.UsoConsumibleResponseDTO;
import com.example.demo.business.api.dto.UsoEquipamientoRequestDTO;
import com.example.demo.business.api.dto.UsoEquipamientoResponseDTO;

public interface UsoService {
    UsoEquipamientoResponseDTO registrarUsoEquipamiento(UsoEquipamientoRequestDTO request);
    UsoConsumibleResponseDTO registrarUsoConsumible(UsoConsumibleRequestDTO request);
}

