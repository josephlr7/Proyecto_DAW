package com.cibertec.tienda.business.data.repository;

import com.cibertec.tienda.business.data.entity.DetalleVenta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @EntityGraph(attributePaths = "producto")
    List<DetalleVenta> findByVentaIdOrderByIdAsc(
            Long ventaId
    );

    @EntityGraph(attributePaths = "producto")
    List<DetalleVenta> findByProductoId(
            Long productoId
    );

    boolean existsByVentaIdAndProductoId(
            Long ventaId,
            Long productoId
    );
}
