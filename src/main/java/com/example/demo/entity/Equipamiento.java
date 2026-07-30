package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipamientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "funcion", nullable = false, length = 255)
    private String funcion;

    @Column(name = "rango_precio", nullable = false, length = 50)
    private String rangoPrecio;

    @Column(name = "ano_adquisicion", nullable = false)
    private Integer anoAdquisicion;

    @Column(name = "horas_uso", nullable = false)
    private Double horasUso = 0.0;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // operativo, no operativo

    @Column(name = "programa_mantenimiento", nullable = false, length = 50)
    private String programaMantenimiento; // anual, bienal, trienal, no corresponde, no considerado

    @Column(name = "programa_mantenimiento_horas")
    private Integer programaMantenimientoHoras;

    @Column(name = "se_cumple_mantenimiento", length = 20)
    private String seCumpleMantenimiento; // Si, No, a veces

    @Column(name = "requiere_consumible", nullable = false)
    private Boolean requiereConsumible = false;

    @Column(name = "tipo_consumible_requerido", length = 50)
    private String tipoConsumibleRequerido; // gas, solvente, metal, otro

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @OneToMany(mappedBy = "equipamiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsoEquipamiento> usos = new ArrayList<>();
}
