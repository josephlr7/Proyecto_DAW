package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UsoEquipamientoResponseDTO(
    Long id,
    Long investigadorId,
    String investigadorNombreCompleto,
    Long equipamientoId,
    String equipamientoNombre,
    String tipoInvestigacion,
    String actividadNombre,
    Double horasUso,
    LocalDate fecha,
    LocalTime hora,
    String observacion
) {}
