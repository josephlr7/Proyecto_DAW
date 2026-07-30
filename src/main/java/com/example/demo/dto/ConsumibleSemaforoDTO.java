package com.example.demo.dto;

public record ConsumibleSemaforoDTO(
    Long id,
    String nombre,
    Double cantidad,
    Double stockMinimo,
    String estadoSemaforo
) {}
