package com.cibertec.tienda.business.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Persona {

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(
            name = "numero_documento",
            nullable = false,
            unique = true,
            length = 8
    )
    private String numeroDocumento;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 9)
    private String telefono;

    @Column(length = 250)
    private String direccion;
}
