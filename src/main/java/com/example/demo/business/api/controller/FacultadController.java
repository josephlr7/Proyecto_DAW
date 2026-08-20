package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.EscuelaDTO;
import com.example.demo.business.api.dto.FacultadDTO;
import com.example.demo.business.domain.service.FacultadService;
import com.example.demo.business.domain.service.EscuelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facultades")
@RequiredArgsConstructor
public class FacultadController {

    private final FacultadService facultadService;
    private final EscuelaService escuelaService;

    @PostMapping
    public ResponseEntity<FacultadDTO> registrarFacultad(@Valid @RequestBody FacultadDTO dto) {
        FacultadDTO creada = facultadService.registrarFacultad(dto);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultadDTO> actualizarFacultad(@PathVariable Long id, @Valid @RequestBody FacultadDTO dto) {
        FacultadDTO actualizada = facultadService.actualizarFacultad(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultadDTO> obtenerPorId(@PathVariable Long id) {
        FacultadDTO facultad = facultadService.obtenerPorId(id);
        return ResponseEntity.ok(facultad);
    }

    @GetMapping
    public ResponseEntity<List<FacultadDTO>> obtenerFacultades() {
        return ResponseEntity.ok(facultadService.obtenerTodos());
    }

    @GetMapping("/{id}/escuelas")
    public ResponseEntity<List<EscuelaDTO>> obtenerEscuelasPorFacultad(@PathVariable Long id) {
        return ResponseEntity.ok(escuelaService.obtenerPorFacultad(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFacultad(@PathVariable Long id) {
        facultadService.eliminarFacultad(id);
        return ResponseEntity.noContent().build();
    }
}
