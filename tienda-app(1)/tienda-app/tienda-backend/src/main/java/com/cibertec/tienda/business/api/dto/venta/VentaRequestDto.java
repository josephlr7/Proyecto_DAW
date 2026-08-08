package com.cibertec.tienda.business.api.dto.venta;

import com.cibertec.tienda.business.data.entity.enums.MetodoPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record VentaRequestDto(

        @NotNull(message = "El cliente es obligatorio")
        @Positive(message = "El ID del cliente debe ser positivo")
        Long clienteId,

        @NotNull(message = "El empleado es obligatorio")
        @Positive(message = "El ID del empleado debe ser positivo")
        Long empleadoId,

        @NotNull(message = "El método de pago es obligatorio")
        MetodoPago metodoPago,

        @DecimalMin(
                value = "0.00",
                message = "El descuento de la venta no puede ser negativo"
        )
        @Digits(
                integer = 8,
                fraction = 2,
                message = "El descuento debe tener hasta 2 decimales"
        )
        BigDecimal descuento,

        @NotEmpty(
                message = "La venta debe contener al menos un producto"
        )
        List<@Valid DetalleVentaRequestDto> detalles
) {
}
