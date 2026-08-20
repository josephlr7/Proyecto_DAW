package com.example.demo.business.api.dto;

public record PersonalLaboratorioResponseDTO(
    Long id,
    String nombres,
    String apellidos,
    String dni,
    String genero,
    String cargo,
    String fotoUrl,
    Boolean esDocente,
    Boolean renacyt,
    Boolean esDocenteInvestigadorUNT,
    String condicion,
    Long laboratorioId
) {}

