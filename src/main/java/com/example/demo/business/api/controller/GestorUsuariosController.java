package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.UsuarioUnificadoRequestDTO;
import com.example.demo.business.domain.service.GestorUsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios-unificado")
@RequiredArgsConstructor
public class GestorUsuariosController {

    private final GestorUsuariosService gestorUsuariosService;

    @PostMapping
    public ResponseEntity<Void> registrarUsuarioYPersona(
            @Valid @RequestBody UsuarioUnificadoRequestDTO request) {
        gestorUsuariosService.registrarUsuarioYPersona(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<java.util.List<com.example.demo.business.api.dto.UsuarioUnificadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(gestorUsuariosService.listarTodos());
    }

    @PutMapping("/{dni}")
    public ResponseEntity<Void> actualizarUsuarioYPersona(
            @PathVariable String dni,
            @Valid @RequestBody UsuarioUnificadoRequestDTO request) {
        gestorUsuariosService.actualizarUsuarioYPersona(dni, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> darDeBaja(@PathVariable String dni) {
        gestorUsuariosService.darDeBaja(dni);
        return ResponseEntity.ok().build();
    }
}
