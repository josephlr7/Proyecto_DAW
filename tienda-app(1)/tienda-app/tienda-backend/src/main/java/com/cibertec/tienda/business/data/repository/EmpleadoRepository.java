package com.cibertec.tienda.business.data.repository;

import com.cibertec.tienda.business.data.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByNumeroDocumento(
            String numeroDocumento
    );

    boolean existsByNumeroDocumento(
            String numeroDocumento
    );

    boolean existsByEmail(
            String email
    );

    boolean existsByNumeroDocumentoAndIdNot(
            String numeroDocumento,
            Long id
    );

    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );

    @Query(
            value = """
                    SELECT e.*
                    FROM empleados e
                    WHERE (
                        :cargo IS NULL
                        OR LOWER(e.cargo)
                            LIKE LOWER(CONCAT('%', :cargo, '%'))
                    )
                    AND (
                        :activo IS NULL
                        OR e.activo = :activo
                    )
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM empleados e
                    WHERE (
                        :cargo IS NULL
                        OR LOWER(e.cargo)
                            LIKE LOWER(CONCAT('%', :cargo, '%'))
                    )
                    AND (
                        :activo IS NULL
                        OR e.activo = :activo
                    )
                    """,
            nativeQuery = true
    )
    Page<Empleado> buscarEmpleados(
            @Param("cargo") String cargo,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
}
