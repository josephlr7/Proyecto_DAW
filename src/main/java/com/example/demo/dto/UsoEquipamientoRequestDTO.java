package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record UsoEquipamientoRequestDTO(
    @NotNull(message = "El ID del investigador es obligatorio")
    Long investigadorId,

    @NotNull(message = "El ID del equipamiento es obligatorio")
    Long equipamientoId,

    @NotBlank(message = "El tipo de investigación es obligatorio")
    @Size(max = 100, message = "El tipo no puede superar los 100 caracteres")
    String tipoInvestigacion,

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    @Size(max = 150, message = "La actividad no puede superar los 150 caracteres")
    String actividadNombre,

    @NotNull(message = "Las horas de uso son obligatorias")
    @DecimalMin(value = "0.1", message = "Las horas de uso deben ser mayores a 0")
    Double horasUso,

    @NotNull(message = "La fecha es obligatoria")
    LocalDate fecha,

    @NotNull(message = "La hora es obligatoria")
    LocalTime hora,

    @Size(max = 255, message = "La observación no puede superar los 255 caracteres")
    String observacion
) {}
