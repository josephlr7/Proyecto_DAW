package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.InvestigadorDTO;
import com.example.demo.business.domain.service.InvestigadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/investigadores")
@RequiredArgsConstructor
public class InvestigadorController {

    private final InvestigadorService investigadorService;

    @PostMapping
    public ResponseEntity<InvestigadorDTO> registrarInvestigador(@Valid @RequestBody InvestigadorDTO dto) {
        InvestigadorDTO creado = investigadorService.registrarInvestigador(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestigadorDTO> obtenerPorId(@PathVariable Long id) {
        InvestigadorDTO inv = investigadorService.obtenerPorId(id);
        return ResponseEntity.ok(inv);
    }

    @GetMapping
    public ResponseEntity<List<InvestigadorDTO>> obtenerTodos() {
        List<InvestigadorDTO> todos = investigadorService.obtenerTodos();
        return ResponseEntity.ok(todos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInvestigador(@PathVariable Long id) {
        investigadorService.eliminarInvestigador(id);
        return ResponseEntity.noContent().build();
    }
}

