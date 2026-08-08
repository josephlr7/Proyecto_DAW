package com.cibertec.tienda.business.api.dto.venta;

import com.cibertec.tienda.business.data.entity.enums.EstadoVenta;
import com.cibertec.tienda.business.data.entity.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VentaResponseDto(

        Long id,

        LocalDateTime fechaVenta,

        Long clienteId,

        String cliente,

        Long empleadoId,

        String empleado,

        BigDecimal subtotal,

        BigDecimal descuento,

        BigDecimal total,

        MetodoPago metodoPago,

        EstadoVenta estado,

        List<DetalleVentaResponseDto> detalles
) {
}
