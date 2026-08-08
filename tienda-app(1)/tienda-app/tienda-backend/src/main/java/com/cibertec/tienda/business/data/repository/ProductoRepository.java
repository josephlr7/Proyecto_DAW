package com.cibertec.tienda.business.data.repository;

import com.cibertec.tienda.business.data.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(
            String sku,
            Long id
    );

    @Procedure(name = "Producto.consultar")
    List<Producto> consultar(
            @Param("p_nombre")
            String nombre,

            @Param("p_marca")
            String marca,

            @Param("p_categoria")
            String categoria,

            @Param("p_activo")
            Boolean activo,

            @Param("p_precio_minimo")
            BigDecimal precioMinimo,

            @Param("p_precio_maximo")
            BigDecimal precioMaximo,

            @Param("p_orden")
            String orden,

            @Param("p_direccion")
            String direccion
    );
}

