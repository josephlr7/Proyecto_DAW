package com.example.demo.business.api.dto;

import jakarta.validation.constraints.Email;
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

    @NotBlank(message = "El área de investigación es obligatoria")
    @Size(max = 100, message = "El área no puede superar los 100 caracteres")
    String areaInvestigacion,

    @NotBlank(message = "Las líneas de investigación son obligatorias")
    @Size(max = 500, message = "Las líneas de investigación no pueden superar los 500 caracteres")
    String lineasInvestigacion,

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede superar los 50 caracteres")
    String categoria,

    @Size(max = 100, message = "El número de resolución no puede superar los 100 caracteres")
    String resolucionNumero,

    @NotBlank(message = "El correo institucional es obligatorio")
    @Email(message = "El formato de correo no es válido")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
    String correoInstitucional,

    @Size(max = 100, message = "La lista de ODS no puede superar los 100 caracteres")
    String ods,

    @NotNull(message = "Debe indicar si posee un sistema de gestión")
    Boolean poseeSistemaGestion
) {}

