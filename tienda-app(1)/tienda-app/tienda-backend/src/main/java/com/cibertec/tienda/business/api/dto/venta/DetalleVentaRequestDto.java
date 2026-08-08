package com.cibertec.tienda.business.api.dto.venta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DetalleVentaRequestDto(

        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID del producto debe ser positivo")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        Integer cantidad,

        @DecimalMin(
                value = "0.00",
                message = "El descuento no puede ser negativo"
        )
        @Digits(
                integer = 8,
                fraction = 2,
                message = "El descuento debe tener hasta 2 decimales"
        )
        BigDecimal descuento
) {
}
