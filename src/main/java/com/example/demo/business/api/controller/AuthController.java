package com.example.demo.business.api.controller;

import com.example.demo.business.api.dto.LoginRequestDto;
import com.example.demo.business.api.dto.LoginResponseDto;
import com.example.demo.business.api.dto.RegistroUsuarioRequestDto;
import com.example.demo.business.data.entity.Usuario;
import com.example.demo.business.data.repository.UsuarioRepository;
import com.example.demo.business.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody RegistroUsuarioRequestDto request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(request.rol().toUpperCase());

        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.password(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        String token = jwtUtils.generarToken(usuario.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(usuario.getUsername(), token, usuario.getRol()));
    }
}
