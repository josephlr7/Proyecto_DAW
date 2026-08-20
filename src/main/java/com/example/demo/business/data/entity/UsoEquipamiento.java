package com.example.demo.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "usos_equipamiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsoEquipamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private com.example.demo.business.security.data.entity.Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamiento_id", nullable = false)
    private Equipamiento equipamiento;

    @Column(name = "tipo_investigacion", nullable = false, length = 100)
    private String tipoInvestigacion; // tesis de pregrado, PICFEDU, etc.

    @Column(name = "actividad_nombre", nullable = false, length = 150)
    private String actividadNombre;

    @Column(name = "horas_uso", nullable = false)
    private Double horasUso;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "nombre_investigador", length = 150)
    private String nombreInvestigador;
}

