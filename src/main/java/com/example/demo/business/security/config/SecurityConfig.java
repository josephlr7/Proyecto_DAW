package com.example.demo.business.security.config;

import com.example.demo.business.security.domain.service.CustomUserDetailsService;
import com.example.demo.business.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/error").permitAll()

                        // ── USOS ──────────────────────────────────────────────────────────
                        // GET (ver listado): ADMIN y PERSONAL
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/usos/**")
                            .hasAnyRole("ADMIN", "PERSONAL", "INVESTIGADOR")
                        // POST (registrar): todos los roles
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/usos/**")
                            .hasAnyRole("ADMIN", "PERSONAL", "INVESTIGADOR")
                        // PUT y DELETE (editar/eliminar): solo ADMIN y PERSONAL
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/usos/**")
                            .hasAnyRole("ADMIN", "PERSONAL")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/usos/**")
                            .hasAnyRole("ADMIN", "PERSONAL")

                        // ── DASHBOARD ─────────────────────────────────────────────────────
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/dashboard/**")
                            .hasAnyRole("ADMIN", "PERSONAL", "INVESTIGADOR")

                        // ── EQUIPAMIENTO ──────────────────────────────────────────────────
                        // GET: ADMIN y PERSONAL (INVESTIGADOR lo necesita solo para el select del form de uso)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/equipamiento/**")
                            .hasAnyRole("ADMIN", "PERSONAL", "INVESTIGADOR")
                        // POST, PUT, DELETE: solo ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/equipamiento/**")
                            .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/equipamiento/**")
                            .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/equipamiento/**")
                            .hasRole("ADMIN")

                        // ── CONSUMIBLE ────────────────────────────────────────────────────
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/consumible/**")
                            .hasAnyRole("ADMIN", "PERSONAL", "INVESTIGADOR")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/consumible/**")
                            .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/consumible/**")
                            .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/consumible/**")
                            .hasRole("ADMIN")

                        // ── LABORATORIOS, FACULTADES, ESCUELAS ────────────────────────────
                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/laboratorios/**", "/api/facultades/**", "/api/escuelas/**")
                            .hasAnyRole("ADMIN", "PERSONAL")
                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/laboratorios/**", "/api/facultades/**", "/api/escuelas/**")
                            .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                "/api/laboratorios/**", "/api/facultades/**", "/api/escuelas/**")
                            .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                "/api/laboratorios/**", "/api/facultades/**", "/api/escuelas/**")
                            .hasRole("ADMIN")

                        // ── USUARIOS Y PERSONAL ───────────────────────────────────────────
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/personal/**").hasRole("ADMIN")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
