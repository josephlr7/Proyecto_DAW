package com.example.demo.controller;

import com.example.demo.dto.UsoConsumibleRequestDTO;
import com.example.demo.dto.UsoConsumibleResponseDTO;
import com.example.demo.dto.UsoEquipamientoRequestDTO;
import com.example.demo.dto.UsoEquipamientoResponseDTO;
import com.example.demo.service.UsoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usos")
@RequiredArgsConstructor
public class UsoController {

    private final UsoService usoService;

    @PostMapping("/equipamiento")
    public ResponseEntity<UsoEquipamientoResponseDTO> registrarUsoEquipamiento(
            @Valid @RequestBody UsoEquipamientoRequestDTO request) {
        UsoEquipamientoResponseDTO response = usoService.registrarUsoEquipamiento(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/consumible")
    public ResponseEntity<UsoConsumibleResponseDTO> registrarUsoConsumible(
            @Valid @RequestBody UsoConsumibleRequestDTO request) {
        UsoConsumibleResponseDTO response = usoService.registrarUsoConsumible(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
