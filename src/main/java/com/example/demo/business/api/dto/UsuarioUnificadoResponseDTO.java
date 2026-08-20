package com.example.demo.business.api.dto;

public record UsuarioUnificadoResponseDTO(
        Long personaId,
        String nombres,
        String apellidos,
        String dni,
        String genero,
        String rolSistema, // ADMIN, PERSONAL, INVESTIGADOR
        Boolean activo,
        // Fields for PersonalLaboratorio
        String cargo,
        String condicion,
        Long laboratorioId,
        Boolean esDocente,
        Boolean renacyt,
        Boolean esDocenteInvestigadorUNT,
        // Fields for Investigador
        String programaEstudios,
        String gradoAcademico
) {}
