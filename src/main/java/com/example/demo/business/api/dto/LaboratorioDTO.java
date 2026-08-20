package com.example.demo.business.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LaboratorioDTO(
    Long id,

    @NotBlank(message = "La facultad es obligatoria")
    @Size(max = 100, message = "La facultad no puede superar los 100 caracteres")
    String facultad,

    @NotBlank(message = "La escuela es obligatoria")
    @Size(max = 100, message = "La escuela no puede superar los 100 caracteres")
    String escuela,

    @NotNull(message = "Debe indicar si posee un sistema de gestión")
    Boolean poseeSistemaGestion
) {}
