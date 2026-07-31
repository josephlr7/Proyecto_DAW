package com.example.demo.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "laboratorios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facultad", nullable = false, length = 100)
    private String facultad;

    @Column(name = "escuela", nullable = false, length = 100)
    private String escuela;

    @Column(name = "area_investigacion", nullable = false, length = 100)
    private String areaInvestigacion;

    @Column(name = "lineas_investigacion", nullable = false, length = 500)
    private String lineasInvestigacion;

    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;

    @Column(name = "resolucion_numero", length = 100)
    private String resolucionNumero;

    @Column(name = "correo_institucional", nullable = false, unique = true, length = 100)
    private String correoInstitucional;

    @Column(name = "ods", length = 100)
    private String ods;

    @Column(name = "posee_sistema_gestion", nullable = false)
    private Boolean poseeSistemaGestion = false;

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonalLaboratorio> personal = new ArrayList<>();

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Equipamiento> equipamientos = new ArrayList<>();

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consumible> consumibles = new ArrayList<>();
}

