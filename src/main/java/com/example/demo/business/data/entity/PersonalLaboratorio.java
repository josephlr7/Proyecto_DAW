package com.example.demo.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "personal_laboratorio")
@PrimaryKeyJoinColumn(name = "personal_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalLaboratorio extends Persona {

    @Column(name = "resolucion_numero", length = 100)
    private String resolucionNumero;

    @Column(name = "cargo", nullable = false, length = 50)
    private String cargo;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "es_docente", nullable = false)
    private Boolean esDocente = false;

    @Column(name = "renacyt", nullable = false)
    private Boolean renacyt = false;

    @Column(name = "es_docente_investigador_unt", nullable = false)
    private Boolean esDocenteInvestigadorUNT = false;

    @Column(name = "condicion", nullable = false, length = 50)
    private String condicion;

    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    private PerfilPersonal perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id")
    private Laboratorio laboratorio;
}

