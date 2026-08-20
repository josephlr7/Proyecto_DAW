package com.example.demo.business.api.controller;

import com.example.demo.business.data.repository.*;
import com.example.demo.business.security.data.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UsuarioRepository usuarioRepository;
    private final PersonalLaboratorioRepository personalRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final FacultadRepository facultadRepository;
    private final EscuelaRepository escuelaRepository;
    private final EquipamientoRepository equipamientoRepository;
    private final ConsumibleRepository consumibleRepository;
    private final UsoEquipamientoRepository usoEquipamientoRepository;
    private final UsoConsumibleRepository usoConsumibleRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("usuarios", usuarioRepository.count());
        stats.put("personal", personalRepository.count());
        stats.put("laboratorios", laboratorioRepository.count());
        stats.put("facultades", facultadRepository.count());
        stats.put("escuelas", escuelaRepository.count());
        stats.put("equipamientos", equipamientoRepository.count());
        stats.put("consumibles", consumibleRepository.count());
        stats.put("usosEquipos", usoEquipamientoRepository.count());
        stats.put("usosConsumibles", usoConsumibleRepository.count());
        return ResponseEntity.ok(stats);
    }
}
