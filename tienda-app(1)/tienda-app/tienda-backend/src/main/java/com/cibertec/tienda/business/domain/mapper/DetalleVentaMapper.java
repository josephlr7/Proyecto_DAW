package com.cibertec.tienda.business.domain.mapper;

import com.cibertec.tienda.business.api.dto.venta.DetalleVentaResponseDto;
import com.cibertec.tienda.business.data.entity.DetalleVenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetalleVentaMapper {

    @Mapping(
            source = "producto.id",
            target = "productoId"
    )
    @Mapping(
            source = "producto.sku",
            target = "sku"
    )
    @Mapping(
            source = "producto.nombre",
            target = "producto"
    )
    DetalleVentaResponseDto toResponseDto(
            DetalleVenta detalle
    );
}
