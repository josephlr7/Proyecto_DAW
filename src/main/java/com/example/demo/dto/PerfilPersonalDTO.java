package com.example.demo.dto;

import java.time.LocalDate;

public record PerfilPersonalDTO(
    LocalDate fechaContratacion,
    String biografia,
    String nroOficina
) {}
