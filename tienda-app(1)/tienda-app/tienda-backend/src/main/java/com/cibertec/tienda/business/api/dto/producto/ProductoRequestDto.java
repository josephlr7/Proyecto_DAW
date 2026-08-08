package com.cibertec.tienda.business.api.dto.producto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoRequestDto(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(
                max = 150,
                message = "El nombre no debe superar los 150 caracteres"
        )
        String nombre,

        @Size(
                max = 500,
                message = "La descripción no debe superar los 500 caracteres"
        )
        String descripcion,

        @NotBlank(message = "El SKU es obligatorio")
        @Size(
                max = 50,
                message = "El SKU no debe superar los 50 caracteres"
        )
        String sku,

        @NotBlank(message = "La marca es obligatoria")
        @Size(
                max = 50,
                message = "La marca no debe superar los 50 caracteres"
        )
        String marca,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(
                max = 50,
                message = "La categoría no debe superar los 50 caracteres"
        )
        String categoria,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(
                value = "0.01",
                message = "El precio debe ser mayor que cero"
        )
        @Digits(
                integer = 8,
                fraction = 2,
                message = "El precio debe tener hasta 8 enteros y 2 decimales"
        )
        BigDecimal precio,

        @DecimalMin(
                value = "0.01",
                message = "El precio de oferta debe ser mayor que cero"
        )
        @Digits(
                integer = 8,
                fraction = 2,
                message = "El precio de oferta debe tener hasta 8 enteros y 2 decimales"
        )
        BigDecimal precioOferta,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(
                message = "El stock no puede ser negativo"
        )
        Integer stock,

        @Size(
                max = 500,
                message = "La URL de la imagen no debe superar los 500 caracteres"
        )
        String imagenUrl
) {
}
