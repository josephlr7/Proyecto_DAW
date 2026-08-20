package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.LaboratorioDTO;
import com.example.demo.business.domain.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    @PostMapping
    public ResponseEntity<LaboratorioDTO> registrarLaboratorio(@Valid @RequestBody LaboratorioDTO dto) {
        LaboratorioDTO creado = laboratorioService.registrarLaboratorio(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioDTO> obtenerPorId(@PathVariable Long id) {
        LaboratorioDTO lab = laboratorioService.obtenerPorId(id);
        return ResponseEntity.ok(lab);
    }

    @GetMapping
    public ResponseEntity<List<LaboratorioDTO>> obtenerTodos() {
        List<LaboratorioDTO> todos = laboratorioService.obtenerTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/consulta")
    public ResponseEntity<org.springframework.data.domain.Page<LaboratorioDTO>> consultar(
            @RequestParam(required = false) String escuela,
            @RequestParam(required = false) String facultad,
            @org.springframework.data.web.PageableDefault(page = 0, size = 5, sort = "escuela")
            org.springframework.data.domain.Pageable pageable
    ) {
        return ResponseEntity.ok(laboratorioService.consultar(escuela, facultad, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratorioDTO> actualizarLaboratorio(@PathVariable Long id, @Valid @RequestBody LaboratorioDTO dto) {
        LaboratorioDTO actualizado = laboratorioService.actualizarLaboratorio(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLaboratorio(@PathVariable Long id) {
        laboratorioService.eliminarLaboratorio(id);
        return ResponseEntity.noContent().build();
    }
}

