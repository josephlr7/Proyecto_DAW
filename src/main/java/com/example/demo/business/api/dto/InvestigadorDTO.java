package com.example.demo.business.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InvestigadorDTO(
    Long id,

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

    @NotBlank(message = "El programa de estudios es obligatorio")
    @Size(max = 100, message = "El programa de estudios no puede superar los 100 caracteres")
    String programaEstudios,

    @NotBlank(message = "El grado académico es obligatorio")
    @Size(max = 50, message = "El grado académico no puede superar los 50 caracteres")
    String gradoAcademico
) {}

