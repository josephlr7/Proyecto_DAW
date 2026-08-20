package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.EscuelaDTO;
import com.example.demo.business.domain.service.EscuelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escuelas")
@RequiredArgsConstructor
public class EscuelaController {

    private final EscuelaService escuelaService;

    @PostMapping
    public ResponseEntity<EscuelaDTO> registrarEscuela(@Valid @RequestBody EscuelaDTO dto) {
        EscuelaDTO creada = escuelaService.registrarEscuela(dto);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscuelaDTO> actualizarEscuela(@PathVariable Long id, @Valid @RequestBody EscuelaDTO dto) {
        EscuelaDTO actualizada = escuelaService.actualizarEscuela(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscuelaDTO> obtenerPorId(@PathVariable Long id) {
        EscuelaDTO escuela = escuelaService.obtenerPorId(id);
        return ResponseEntity.ok(escuela);
    }

    @GetMapping
    public ResponseEntity<List<EscuelaDTO>> obtenerTodas() {
        return ResponseEntity.ok(escuelaService.obtenerTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEscuela(@PathVariable Long id) {
        escuelaService.eliminarEscuela(id);
        return ResponseEntity.noContent().build();
    }
}
