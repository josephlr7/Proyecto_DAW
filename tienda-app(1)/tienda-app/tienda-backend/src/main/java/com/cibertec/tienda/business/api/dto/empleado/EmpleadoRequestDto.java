package com.cibertec.tienda.business.api.dto.empleado;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoRequestDto(

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(
                max = 100,
                message = "Los nombres no deben superar los 100 caracteres"
        )
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(
                max = 100,
                message = "Los apellidos no deben superar los 100 caracteres"
        )
        String apellidos,

        @NotBlank(message = "El número de documento es obligatorio")
        @Pattern(
                regexp = "^\\d{8}$",
                message = "El número de documento debe tener exactamente 8 dígitos"
        )
        String numeroDocumento,

        @Email(message = "El correo no tiene un formato válido")
        @Size(
                max = 150,
                message = "El correo no debe superar los 150 caracteres"
        )
        String email,

        @Pattern(
                regexp = "^9\\d{8}$",
                message = "El teléfono debe tener 9 dígitos y comenzar con 9"
        )
        String telefono,

        @Size(
                max = 250,
                message = "La dirección no debe superar los 250 caracteres"
        )
        String direccion,

        @NotBlank(message = "El cargo es obligatorio")
        @Size(
                max = 100,
                message = "El cargo no debe superar los 100 caracteres"
        )
        String cargo,

        @NotNull(message = "El sueldo es obligatorio")
        @DecimalMin(
                value = "0.01",
                message = "El sueldo debe ser mayor que cero"
        )
        @Digits(
                integer = 8,
                fraction = 2,
                message = "El sueldo debe tener hasta 8 enteros y 2 decimales"
        )
        BigDecimal sueldo,

        @NotNull(message = "La fecha de contratación es obligatoria")
        @PastOrPresent(
                message = "La fecha de contratación no puede ser futura"
        )
        LocalDate fechaContratacion
) {
}
