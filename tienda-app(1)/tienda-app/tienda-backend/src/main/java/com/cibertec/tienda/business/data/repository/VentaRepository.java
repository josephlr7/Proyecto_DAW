package com.cibertec.tienda.business.data.repository;

import com.cibertec.tienda.business.data.entity.Venta;
import com.cibertec.tienda.business.data.entity.enums.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @EntityGraph(
            attributePaths = {
                    "cliente",
                    "empleado",
                    "detalles",
                    "detalles.producto"
            }
    )
    Optional<Venta> findOneById(Long id);

    Page<Venta> findByClienteId(
            Long clienteId,
            Pageable pageable
    );

    Page<Venta> findByEmpleadoId(
            Long empleadoId,
            Pageable pageable
    );

    Page<Venta> findByEstado(
            EstadoVenta estado,
            Pageable pageable
    );

    Page<Venta> findByFechaVentaBetween(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable
    );
}
