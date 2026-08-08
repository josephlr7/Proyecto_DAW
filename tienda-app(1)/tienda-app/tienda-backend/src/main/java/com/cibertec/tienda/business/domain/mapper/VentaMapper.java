package com.cibertec.tienda.business.domain.mapper;

import com.cibertec.tienda.business.api.dto.venta.VentaResponseDto;
import com.cibertec.tienda.business.data.entity.Persona;
import com.cibertec.tienda.business.data.entity.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = DetalleVentaMapper.class
)
public interface VentaMapper {

    @Mapping(
            source = "cliente.id",
            target = "clienteId"
    )
    @Mapping(
            source = "cliente",
            target = "cliente",
            qualifiedByName = "nombreCompleto"
    )
    @Mapping(
            source = "empleado.id",
            target = "empleadoId"
    )
    @Mapping(
            source = "empleado",
            target = "empleado",
            qualifiedByName = "nombreCompleto"
    )
    VentaResponseDto toResponseDto(Venta venta);

    @Named("nombreCompleto")
    default String nombreCompleto(Persona persona) {
        if (persona == null) {
            return null;
        }

        return persona.getNombres()
                + " "
                + persona.getApellidos();
    }
}
