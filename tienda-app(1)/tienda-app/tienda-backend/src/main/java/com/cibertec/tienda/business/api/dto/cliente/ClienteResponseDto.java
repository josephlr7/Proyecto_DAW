package com.cibertec.tienda.business.api.dto.cliente;

import java.time.LocalDateTime;

public record ClienteResponseDto(

        Long id,

        String nombres,

        String apellidos,

        String numeroDocumento,

        String email,

        String telefono,

        String direccion,

        boolean activo,

        LocalDateTime fechaCreacion,

        LocalDateTime fechaActualizacion
) {
}
