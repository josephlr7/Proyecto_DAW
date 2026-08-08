package com.cibertec.tienda.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "productos")
@NamedStoredProcedureQuery(
        name = "Producto.consultar",
        procedureName = "sp_producto_consultar",
        resultClasses = Producto.class,
        parameters = {
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_nombre",
                        type = String.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_marca",
                        type = String.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_categoria",
                        type = String.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_activo",
                        type = Boolean.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_precio_minimo",
                        type = BigDecimal.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_precio_maximo",
                        type = BigDecimal.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_orden",
                        type = String.class
                ),
                @StoredProcedureParameter(
                        mode = ParameterMode.IN,
                        name = "p_direccion",
                        type = String.class
                )
        }
)
/*
name: nombre con el que Java identifica la operación.
procedureName: nombre real del procedimiento en MySQL.
resultClasses: indica que el resultado debe convertirse en objetos Producto.
parameters: declara los ocho parámetros IN.
*/
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 150
    )
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String sku;

    @Column(
            nullable = false,
            length = 50
    )
    private String marca;

    @Column(
            nullable = false,
            length = 50
    )
    private String categoria;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precio;

    @Column(
            name = "precio_oferta",
            precision = 10,
            scale = 2
    )
    private BigDecimal precioOferta;

    @Column(nullable = false)
    private Integer stock;

    @Column(
            name = "imagen_url",
            length = 500
    )
    private String imagenUrl;

    @Column(nullable = false)
    private boolean activo;

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;


    // REGLA DE NEGOCIO

    public boolean validarStock(int stock){
        if (stock > 0){
            return true;
        }else {
            return false;
        }
    }
}