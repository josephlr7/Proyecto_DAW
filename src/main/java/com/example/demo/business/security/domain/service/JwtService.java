package com.example.demo.business.security.domain.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;
    private final long expiration;

    public JwtService(
            @Value("${security.jwt.secret}")
            String secret,

            @Value("${security.jwt.expiration}")
            long expiration
    ) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public String generarToken(
            UserDetails userDetails
    ) {
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = Map.of(
                "roles",
                roles
        );

        Date fechaCreacion = new Date();

        Date fechaExpiracion = new Date(
                fechaCreacion.getTime() + expiration
            );

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(fechaCreacion)
                .expiration(fechaExpiracion)
                .signWith(
                        obtenerClave(),
                        Jwts.SIG.HS256
                )
                .compact();
    }

    public String obtenerUsername(String token) {
        return obtenerClaims(token).getSubject();
    }

    public boolean esTokenValido(
            String token,
            UserDetails userDetails
    ) {
        String username = obtenerUsername(token);

        return username.equals(
                userDetails.getUsername()
        ) && !estaExpirado(token);
    }

    public long obtenerTiempoExpiracion() {
        return expiration;
    }

    private boolean estaExpirado(String token) {
        Date fechaExpiracion =
                obtenerClaims(token).getExpiration();

        return fechaExpiracion.before(new Date());
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClave() {
        byte[] keyBytes =
                Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
