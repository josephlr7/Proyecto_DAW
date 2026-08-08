package com.cibertec.tienda.business.data.repository;

import com.cibertec.tienda.business.data.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Consulta derivada:
    // buscar un cliente por su número de documento
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);

    // Consulta derivada:
    // verificar si el documento ya está registrado
    boolean existsByNumeroDocumento(
            String numeroDocumento
    );

    // Consulta derivada:
    // verificar si el correo ya está registrado
    boolean existsByEmail(
            String email
    );

    // Se utilizarán al actualizar un cliente
    boolean existsByNumeroDocumentoAndIdNot(
            String numeroDocumento,
            Long id
    );

    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );

    // Consulta JPQL con parámetros,
    // paginación y ordenamiento
    @Query("""
            SELECT c
            FROM Cliente c
            WHERE (
                :nombre IS NULL
                OR LOWER(c.nombres)
                    LIKE LOWER(CONCAT('%', :nombre, '%'))
            )
            AND (
                :activo IS NULL
                OR c.activo = :activo
            )
            """)
    Page<Cliente> buscarClientes(
            @Param("nombre") String nombre,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
}
