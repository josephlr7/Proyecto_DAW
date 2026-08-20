package com.example.demo.business.security.api.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UsuarioResponseDto(

        Long id,

        String username,

        String nombre,

        boolean activo,

        LocalDateTime fechaCreacion,

        Set<String> roles
) {
}
