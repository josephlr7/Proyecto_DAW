package com.example.demo.business.security.domain.service.impl;

import com.example.demo.business.api.exception.RecursoDuplicadoException;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.security.api.dto.LoginRequestDto;
import com.example.demo.business.security.api.dto.LoginResponseDto;
import com.example.demo.business.security.api.dto.RegistroUsuarioRequestDto;
import com.example.demo.business.security.api.dto.UsuarioResponseDto;
import com.example.demo.business.security.data.entity.NombreRol;
import com.example.demo.business.security.data.entity.Rol;
import com.example.demo.business.security.data.entity.Usuario;
import com.example.demo.business.security.data.repository.RolRepository;
import com.example.demo.business.security.data.repository.UsuarioRepository;
import com.example.demo.business.security.domain.service.AuthService;
import com.example.demo.business.security.domain.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UsuarioResponseDto registrar(
            RegistroUsuarioRequestDto requestDto
    ) {
        String username =
                normalizarUsername(requestDto.username());

        if (usuarioRepository
                .existsByUsernameIgnoreCase(username)) {
            throw new RecursoDuplicadoException(
                    "El username ya está registrado"
            );
        }

        // Asigna el rol PERSONAL por defecto al registrar
        Rol rolPorDefecto = rolRepository
                .findByNombre(NombreRol.PERSONAL)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el rol PERSONAL"
                        )
                );

        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(
                passwordEncoder.encode(
                        requestDto.password()
                )
        );
        usuario.setNombre(
                requestDto.nombre().trim()
        );
        usuario.agregarRol(rolPorDefecto);

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        return convertirAResponse(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(
            LoginRequestDto requestDto
    ) {
        String username =
                normalizarUsername(requestDto.username());

        UsernamePasswordAuthenticationToken solicitud =
                new UsernamePasswordAuthenticationToken(
                        username,
                        requestDto.password()
                );

        UserDetails userDetails =
                (UserDetails) authenticationManager
                        .authenticate(solicitud)
                        .getPrincipal();

        String token =
                jwtService.generarToken(userDetails);

        Set<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .orElse(null);
        String nombre = usuario != null ? usuario.getNombre() : userDetails.getUsername();

        return new LoginResponseDto(
                token,
                "Bearer",
                jwtService.obtenerTiempoExpiracion(),
                userDetails.getUsername(),
                nombre,
                roles
        );
    }

    private String normalizarUsername(
            String username
    ) {
        return username
                .trim()
                .toLowerCase();
    }

    private UsuarioResponseDto convertirAResponse(
            Usuario usuario
    ) {
        Set<String> roles = usuario
                .getRoles()
                .stream()
                .map(rol ->
                        "ROLE_" + rol.getNombre().name()
                )
                .collect(Collectors.toSet());

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.isActivo(),
                usuario.getFechaCreacion(),
                roles
        );
    }
}
