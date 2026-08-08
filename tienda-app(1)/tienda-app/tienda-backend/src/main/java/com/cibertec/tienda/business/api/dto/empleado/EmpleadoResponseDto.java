package com.cibertec.tienda.business.api.dto.empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoResponseDto(

        Long id,

        String nombres,

        String apellidos,

        String numeroDocumento,

        String email,

        String telefono,

        String direccion,

        String cargo,

        BigDecimal sueldo,

        LocalDate fechaContratacion,

        boolean activo
) {
}
