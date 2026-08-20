package com.example.demo.business.security.api.controller;

import com.example.demo.business.security.api.dto.LoginRequestDto;
import com.example.demo.business.security.api.dto.LoginResponseDto;
import com.example.demo.business.security.api.dto.RegistroUsuarioRequestDto;
import com.example.demo.business.security.api.dto.UsuarioResponseDto;
import com.example.demo.business.security.domain.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registrar(
            @Valid
            @RequestBody
            RegistroUsuarioRequestDto requestDto
    ) {
        UsuarioResponseDto usuarioRegistrado =
                authService.registrar(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioRegistrado);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid
            @RequestBody
            LoginRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                authService.login(requestDto)
        );
    }
}
