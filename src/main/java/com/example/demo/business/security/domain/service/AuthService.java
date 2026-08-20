package com.example.demo.business.security.domain.service;

import com.example.demo.business.security.api.dto.LoginRequestDto;
import com.example.demo.business.security.api.dto.LoginResponseDto;
import com.example.demo.business.security.api.dto.RegistroUsuarioRequestDto;
import com.example.demo.business.security.api.dto.UsuarioResponseDto;

public interface AuthService {

    UsuarioResponseDto registrar(
            RegistroUsuarioRequestDto requestDto
    );

    LoginResponseDto login(
            LoginRequestDto requestDto
    );
}
