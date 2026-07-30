package com.example.demo.dto;

public record EquipamientoSemaforoDTO(
    Long id,
    String nombre,
    Double horasUso,
    Integer programaMantenimientoHoras,
    String estadoSemaforo
) {}
