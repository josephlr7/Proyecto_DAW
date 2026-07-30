package com.example.demo.controller;

import com.example.demo.dto.PersonalLaboratorioRequestDTO;
import com.example.demo.dto.PersonalLaboratorioResponseDTO;
import com.example.demo.service.PersonalLaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personal")
@RequiredArgsConstructor
public class PersonalLaboratorioController {

    private final PersonalLaboratorioService personalService;

    @PostMapping
    public ResponseEntity<PersonalLaboratorioResponseDTO> registrarPersonal(@Valid @RequestBody PersonalLaboratorioRequestDTO request) {
        PersonalLaboratorioResponseDTO creado = personalService.registrarPersonal(request);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalLaboratorioResponseDTO> actualizarPersonal(
            @PathVariable Long id,
            @Valid @RequestBody PersonalLaboratorioRequestDTO request) {
        PersonalLaboratorioResponseDTO actualizado = personalService.actualizarPersonal(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalLaboratorioResponseDTO> obtenerPorId(@PathVariable Long id) {
        PersonalLaboratorioResponseDTO personal = personalService.obtenerPorId(id);
        return ResponseEntity.ok(personal);
    }

    @GetMapping
    public ResponseEntity<List<PersonalLaboratorioResponseDTO>> obtenerTodos() {
        List<PersonalLaboratorioResponseDTO> todos = personalService.obtenerTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/renacyt")
    public ResponseEntity<List<PersonalLaboratorioResponseDTO>> obtenerInvestigadoresRenacyt() {
        List<PersonalLaboratorioResponseDTO> renacytList = personalService.obtenerInvestigadoresRenacyt();
        return ResponseEntity.ok(renacytList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersonal(@PathVariable Long id) {
        personalService.eliminarPersonal(id);
        return ResponseEntity.noContent().build();
    }
}
