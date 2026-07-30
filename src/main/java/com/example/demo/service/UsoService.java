package com.example.demo.service;

import com.example.demo.dto.UsoConsumibleRequestDTO;
import com.example.demo.dto.UsoConsumibleResponseDTO;
import com.example.demo.dto.UsoEquipamientoRequestDTO;
import com.example.demo.dto.UsoEquipamientoResponseDTO;

public interface UsoService {
    UsoEquipamientoResponseDTO registrarUsoEquipamiento(UsoEquipamientoRequestDTO request);
    UsoConsumibleResponseDTO registrarUsoConsumible(UsoConsumibleRequestDTO request);
}
