package com.example.demo.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consumibles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consumible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @Column(name = "empresa", nullable = false, length = 100)
    private String empresa;

    @Column(name = "estado_adquirido", nullable = false, length = 20)
    private String estadoAdquirido; // liquido, solido, gaseoso

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo; // plastico, reactivo, solvente, colorante, fijador, otro

    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida; // gramos, unidades, mililitros, otro

    @Column(name = "funcion", length = 255)
    private String funcion;

    @Column(name = "rango_precio", nullable = false, length = 50)
    private String rangoPrecio;

    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "cantidad", nullable = false)
    private Double cantidad = 0.0;

    @Column(name = "stock_minimo", nullable = false)
    private Double stockMinimo = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "consumible", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsoConsumible> usos = new ArrayList<>();
}

