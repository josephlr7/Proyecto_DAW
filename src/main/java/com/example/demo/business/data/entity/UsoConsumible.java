package com.example.demo.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "usos_consumible")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsoConsumible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private com.example.demo.business.security.data.entity.Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumible_id", nullable = false)
    private Consumible consumible;

    @Column(name = "tipo_investigacion", nullable = false, length = 100)
    private String tipoInvestigacion;

    @Column(name = "actividad_nombre", nullable = false, length = 150)
    private String actividadNombre;

    @Column(name = "cantidad", nullable = false)
    private Double cantidad;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "nombre_investigador", length = 150)
    private String nombreInvestigador;
}

