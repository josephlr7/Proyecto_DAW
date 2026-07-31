package com.example.demo.business.api.dto;

import java.time.LocalDate;

public record PerfilPersonalDTO(
    LocalDate fechaContratacion,
    String biografia,
    String nroOficina
) {}

