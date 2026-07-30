package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ConsumibleDTO(
    Long id,

    @NotBlank(message = "El nombre del consumible es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    String nombre,

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede superar los 50 caracteres")
    String marca,

    @NotBlank(message = "La empresa es obligatoria")
    @Size(max = 100, message = "La empresa no puede superar los 100 caracteres")
    String empresa,

    @NotBlank(message = "El estado adquirido es obligatorio")
    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    String estadoAdquirido,

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    String tipo,

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(max = 50, message = "La unidad de medida no puede superar los 50 caracteres")
    String unidadMedida,

    @Size(max = 255, message = "La función no puede superar los 255 caracteres")
    String funcion,

    @NotBlank(message = "El rango de precio es obligatorio")
    @Size(max = 50, message = "El rango de precio no puede superar los 50 caracteres")
    String rangoPrecio,

    LocalDate fechaAdquisicion,
    LocalDate fechaVencimiento,

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", message = "La cantidad no puede ser negativa")
    Double cantidad,

    @NotNull(message = "El stock mínimo es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock mínimo no puede ser negativo")
    Double stockMinimo,

    @NotNull(message = "El ID del laboratorio es obligatorio")
    Long laboratorioId
) {}
