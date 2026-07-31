package com.example.demo.business.api.dto;

public record LoginResponseDto(
    String username,
    String token,
    String rol
) {}
