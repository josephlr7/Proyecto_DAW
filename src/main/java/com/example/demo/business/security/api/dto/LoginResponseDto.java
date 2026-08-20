package com.example.demo.business.security.api.dto;

import java.util.Set;

public record LoginResponseDto(

        String token,

        String tipo,

        long expiresIn,

        String username,
        
        String nombre,

        Set<String> roles
) {
}
