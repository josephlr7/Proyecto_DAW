package com.example.demo.business.api.dto;

public record EquipamientoSemaforoDTO(
    Long id,
    String nombre,
    Double horasUso,
    Integer programaMantenimientoHoras,
    String estadoSemaforo
) {}

