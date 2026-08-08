package com.cibertec.tienda.business.domain.mapper;

import com.cibertec.tienda.business.api.dto.empleado.EmpleadoRequestDto;
import com.cibertec.tienda.business.api.dto.empleado.EmpleadoResponseDto;
import com.cibertec.tienda.business.data.entity.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {

    EmpleadoResponseDto toResponseDto(Empleado empleado);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Empleado toEntity(EmpleadoRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    void actualizarEntidad(
            EmpleadoRequestDto requestDto,
            @MappingTarget Empleado empleado
    );
}
