package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.EquipamientoDTO;
import com.example.demo.business.api.dto.EquipamientoSemaforoDTO;
import com.example.demo.business.domain.service.EquipamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipamiento")
@RequiredArgsConstructor
public class EquipamientoController {

    private final EquipamientoService equipamientoService;

    @PostMapping
    public ResponseEntity<EquipamientoDTO> registrarEquipamiento(@Valid @RequestBody EquipamientoDTO dto) {
        EquipamientoDTO creado = equipamientoService.registrarEquipamiento(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamientoDTO> actualizarEquipamiento(
            @PathVariable Long id,
            @Valid @RequestBody EquipamientoDTO dto) {
        EquipamientoDTO actualizado = equipamientoService.actualizarEquipamiento(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamientoDTO> obtenerPorId(@PathVariable Long id) {
        EquipamientoDTO equipamiento = equipamientoService.obtenerPorId(id);
        return ResponseEntity.ok(equipamiento);
    }

    @GetMapping
    public ResponseEntity<List<EquipamientoDTO>> obtenerTodos() {
        List<EquipamientoDTO> todos = equipamientoService.obtenerTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/laboratorio/{laboratorioId}")
    public ResponseEntity<List<EquipamientoDTO>> obtenerPorLaboratorioId(@PathVariable Long laboratorioId) {
        List<EquipamientoDTO> equipos = equipamientoService.obtenerPorLaboratorioId(laboratorioId);
        return ResponseEntity.ok(equipos);
    }

    @GetMapping("/laboratorio/{laboratorioId}/semaforo")
    public ResponseEntity<List<EquipamientoSemaforoDTO>> obtenerReporteSemaforo(@PathVariable Long laboratorioId) {
        List<EquipamientoSemaforoDTO> reporte = equipamientoService.obtenerReporteSemaforo(laboratorioId);
        return ResponseEntity.ok(reporte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipamiento(@PathVariable Long id) {
        equipamientoService.eliminarEquipamiento(id);
        return ResponseEntity.noContent().build();
    }
}

