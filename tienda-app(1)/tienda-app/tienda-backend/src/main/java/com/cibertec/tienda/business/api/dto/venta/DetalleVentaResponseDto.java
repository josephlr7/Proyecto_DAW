package com.cibertec.tienda.business.api.dto.venta;

import java.math.BigDecimal;

public record DetalleVentaResponseDto(
        Long id,

        Long productoId,

        String sku,

        String producto,

        Integer cantidad,

        BigDecimal precioUnitario,

        BigDecimal descuento,

        BigDecimal subtotal
) {
}
