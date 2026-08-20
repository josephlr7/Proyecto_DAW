package com.example.demo.business.api.dto;

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
    String nombreInvestigador,
    Double horasUso,
    LocalDate fecha,
    LocalTime hora,
    String observacion
) {}

