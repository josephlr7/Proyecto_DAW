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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultad_id", nullable = false)
    private Facultad facultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escuela_id", nullable = false)
    private Escuela escuela;

    @Column(name = "posee_sistema_gestion", nullable = false)
    private Boolean poseeSistemaGestion = false;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonalLaboratorio> personal = new ArrayList<>();

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Equipamiento> equipamientos = new ArrayList<>();

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consumible> consumibles = new ArrayList<>();
}

