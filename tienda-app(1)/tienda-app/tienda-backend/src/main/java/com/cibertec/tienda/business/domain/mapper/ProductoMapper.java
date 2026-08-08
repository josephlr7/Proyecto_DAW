package com.cibertec.tienda.business.domain.mapper;

import com.cibertec.tienda.business.api.dto.producto.ProductoRequestDto;
import com.cibertec.tienda.business.api.dto.producto.ProductoResponseDto;
import com.cibertec.tienda.business.data.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoResponseDto toResponseDto(Producto producto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Producto toEntity(ProductoRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarEntidad(
            ProductoRequestDto requestDto,
            @MappingTarget Producto producto
    );
}
