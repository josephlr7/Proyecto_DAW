package com.example.demo.business.security.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroUsuarioRequestDto(

        @NotBlank(message = "El username es obligatorio")
        @Size(
                min = 4,
                max = 50,
                message = "El username debe tener entre 4 y 50 caracteres"
        )
        @Pattern(
                regexp = "^[a-zA-Z0-9._-]+$",
                message = "El username contiene caracteres no permitidos"
        )
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(
                min = 8,
                max = 72,
                message = "La contraseña debe tener entre 8 y 72 caracteres"
        )
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "La contraseña debe contener una mayúscula, una minúscula y un número"
        )
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(
                max = 100,
                message = "El nombre no debe superar los 100 caracteres"
        )
        String nombre
) {
}
