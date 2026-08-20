package com.example.demo.business.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PersonalLaboratorioRequestDTO(
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    String nombres,

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    String apellidos,

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 20, message = "El DNI no puede superar los 20 caracteres")
    String dni,

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 20, message = "El género no puede superar los 20 caracteres")
    String genero,

    @NotBlank(message = "El cargo es obligatorio")
    @Size(max = 50, message = "El cargo no puede superar los 50 caracteres")
    String cargo,

    @Size(max = 255, message = "La URL de la foto no puede superar los 255 caracteres")
    String fotoUrl,

    @NotNull(message = "Debe indicar si es docente")
    Boolean esDocente,

    @NotNull(message = "Debe indicar si está clasificado en Renacyt")
    Boolean renacyt,

    @NotNull(message = "Debe indicar si es docente investigador UNT")
    Boolean esDocenteInvestigadorUNT,

    @NotBlank(message = "La condición es obligatoria")
    @Size(max = 50, message = "La condición no puede superar los 50 caracteres")
    String condicion,

    Long laboratorioId
) {}

