package com.cibertec.tienda.business.api.dto.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDto(

        Long id,

        String nombre,

        String descripcion,

        String sku,

        String marca,

        String categoria,

        BigDecimal precio,

        BigDecimal precioOferta,

        Integer stock,

        String imagenUrl,

        boolean activo,

        LocalDateTime fechaCreacion,

        LocalDateTime fechaActualizacion
) {
}
