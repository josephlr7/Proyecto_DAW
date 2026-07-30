package com.example.demo.controller;

import com.example.demo.dto.LaboratorioDTO;
import com.example.demo.service.LaboratorioService;
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
}
