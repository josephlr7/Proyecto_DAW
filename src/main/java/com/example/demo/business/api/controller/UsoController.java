package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.UsoConsumibleRequestDTO;
import com.example.demo.business.api.dto.UsoConsumibleResponseDTO;
import com.example.demo.business.api.dto.UsoEquipamientoRequestDTO;
import com.example.demo.business.api.dto.UsoEquipamientoResponseDTO;
import com.example.demo.business.domain.service.UsoService;
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

    @org.springframework.web.bind.annotation.GetMapping("/equipamiento")
    public ResponseEntity<java.util.List<UsoEquipamientoResponseDTO>> obtenerUsosEquipamiento() {
        return ResponseEntity.ok(usoService.obtenerUsosEquipamiento());
    }

    @org.springframework.web.bind.annotation.GetMapping("/consumible")
    public ResponseEntity<java.util.List<UsoConsumibleResponseDTO>> obtenerUsosConsumible() {
        return ResponseEntity.ok(usoService.obtenerUsosConsumible());
    }

    @org.springframework.web.bind.annotation.PutMapping("/equipamiento/{id}")
    public ResponseEntity<UsoEquipamientoResponseDTO> editarUsoEquipamiento(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @Valid @RequestBody UsoEquipamientoRequestDTO request) {
        return ResponseEntity.ok(usoService.editarUsoEquipamiento(id, request));
    }

    @org.springframework.web.bind.annotation.PutMapping("/consumible/{id}")
    public ResponseEntity<UsoConsumibleResponseDTO> editarUsoConsumible(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @Valid @RequestBody UsoConsumibleRequestDTO request) {
        return ResponseEntity.ok(usoService.editarUsoConsumible(id, request));
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/equipamiento/{id}")
    public ResponseEntity<Void> eliminarUsoEquipamiento(@org.springframework.web.bind.annotation.PathVariable Long id) {
        usoService.eliminarUsoEquipamiento(id);
        return ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/consumible/{id}")
    public ResponseEntity<Void> eliminarUsoConsumible(@org.springframework.web.bind.annotation.PathVariable Long id) {
        usoService.eliminarUsoConsumible(id);
        return ResponseEntity.noContent().build();
    }
}

