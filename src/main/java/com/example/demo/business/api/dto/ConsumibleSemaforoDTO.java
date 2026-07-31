package com.example.demo.business.api.dto;

public record ConsumibleSemaforoDTO(
    Long id,
    String nombre,
    Double cantidad,
    Double stockMinimo,
    String estadoSemaforo
) {}

