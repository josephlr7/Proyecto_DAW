package com.example.demo.business.security.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String username;

    @Column(
            nullable = false,
            length = 100
    )
    private String password;

    @Column(
            nullable = false,
            length = 100
    )
    private String nombre;

    @Column(nullable = false)
    private boolean activo;

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = {
                    @JoinColumn(
                            name = "usuario_id"
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "rol_id"
                    )
            }
    )
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        activo = true;

        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public void agregarRol(Rol rol) {
        roles.add(rol);
    }
}
