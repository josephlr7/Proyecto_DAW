package com.cibertec.tienda.business.domain.mapper;

import com.cibertec.tienda.business.api.dto.cliente.ClienteRequestDto;
import com.cibertec.tienda.business.api.dto.cliente.ClienteResponseDto;
import com.cibertec.tienda.business.data.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponseDto toResponseDto(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    Cliente toEntity(ClienteRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizarEntidad(
            ClienteRequestDto requestDto,
            @MappingTarget Cliente cliente
    );
}
