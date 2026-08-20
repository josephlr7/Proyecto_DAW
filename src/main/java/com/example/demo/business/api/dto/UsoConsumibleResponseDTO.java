package com.example.demo.business.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record UsoConsumibleResponseDTO(
    Long id,
    Long investigadorId,
    String investigadorNombreCompleto,
    Long consumibleId,
    String consumibleNombre,
    String tipoInvestigacion,
    String actividadNombre,
    String nombreInvestigador,
    Double cantidad,
    LocalDate fecha,
    LocalTime hora,
    String observacion
) {}

