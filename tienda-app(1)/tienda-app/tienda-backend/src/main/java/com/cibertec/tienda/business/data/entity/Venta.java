package com.cibertec.tienda.business.data.entity;

import com.cibertec.tienda.business.data.entity.enums.EstadoVenta;
import com.cibertec.tienda.business.data.entity.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "cliente_id",
            nullable = false
    )
    private Cliente cliente;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "empleado_id",
            nullable = false
    )
    private Empleado empleado;

    @Column(
            name = "fecha_venta",
            nullable = false
    )
    private LocalDateTime fechaVenta;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal subtotal;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal descuento;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "metodo_pago",
            nullable = false,
            length = 30
    )
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private EstadoVenta estado;

    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleVenta> detalles =
            new ArrayList<>();

    public void agregarDetalle(
            DetalleVenta detalle
    ) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public void removerDetalle(
            DetalleVenta detalle
    ) {
        detalles.remove(detalle);
        detalle.setVenta(null);
    }
}
