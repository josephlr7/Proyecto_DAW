package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.ConsumibleDTO;
import com.example.demo.business.api.dto.ConsumibleSemaforoDTO;
import com.example.demo.business.domain.service.ConsumibleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/consumibles")
@RequiredArgsConstructor
public class ConsumibleController {

    private final ConsumibleService consumibleService;

    @PostMapping
    public ResponseEntity<ConsumibleDTO> registrarConsumible(@Valid @RequestBody ConsumibleDTO dto) {
        ConsumibleDTO creado = consumibleService.registrarConsumible(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsumibleDTO> actualizarConsumible(
            @PathVariable Long id,
            @Valid @RequestBody ConsumibleDTO dto) {
        ConsumibleDTO actualizado = consumibleService.actualizarConsumible(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumibleDTO> obtenerPorId(@PathVariable Long id) {
        ConsumibleDTO consumible = consumibleService.obtenerPorId(id);
        return ResponseEntity.ok(consumible);
    }

    @GetMapping
    public ResponseEntity<List<ConsumibleDTO>> obtenerTodos() {
        List<ConsumibleDTO> todos = consumibleService.obtenerTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/laboratorio/{laboratorioId}")
    public ResponseEntity<List<ConsumibleDTO>> obtenerPorLaboratorioId(@PathVariable Long laboratorioId) {
        List<ConsumibleDTO> consumibles = consumibleService.obtenerPorLaboratorioId(laboratorioId);
        return ResponseEntity.ok(consumibles);
    }

    @GetMapping("/laboratorio/{laboratorioId}/semaforo")
    public ResponseEntity<List<ConsumibleSemaforoDTO>> obtenerReporteSemaforo(@PathVariable Long laboratorioId) {
        List<ConsumibleSemaforoDTO> reporte = consumibleService.obtenerReporteSemaforo(laboratorioId);
        return ResponseEntity.ok(reporte);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConsumible(@PathVariable Long id) {
        consumibleService.eliminarConsumible(id);
        return ResponseEntity.noContent().build();
    }
}

