package com.example.demo.business.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUnificadoRequestDTO(
    @NotBlank(message = "Los nombres son obligatorios")
    String nombres,
    
    @NotBlank(message = "Los apellidos son obligatorios")
    String apellidos,
    
    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 20, message = "El DNI no puede superar los 20 caracteres")
    String dni,
    
    @NotBlank(message = "El género es obligatorio")
    String genero,
    
    @NotBlank(message = "El rol en el sistema es obligatorio (ADMIN, PERSONAL, INVESTIGADOR)")
    String rolSistema,
    
    // Campos específicos para Personal de Laboratorio
    String cargo,
    String condicion,
    Long laboratorioId,
    Boolean esDocente,
    Boolean renacyt,
    Boolean esDocenteInvestigadorUNT,
    
    // Campos específicos para Investigador
    String programaEstudios,
    String gradoAcademico
) {}
