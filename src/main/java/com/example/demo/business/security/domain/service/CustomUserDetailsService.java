package com.example.demo.business.security.domain.service;

import com.example.demo.business.security.data.entity.Usuario;
import com.example.demo.business.security.data.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(
            UsuarioRepository usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        String usernameNormalizado =
                username.trim().toLowerCase();

        Usuario usuario = usuarioRepository
                .findByUsernameIgnoreCase(usernameNormalizado)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario o contraseña incorrectos"
                        )
                );

        String[] autoridades = usuario.getRoles()
                .stream()
                .map(rol ->
                        "ROLE_" + rol.getNombre().name()
                )
                .toArray(String[]::new);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(autoridades)
                .disabled(!usuario.isActivo())
                .build();
    }
}
