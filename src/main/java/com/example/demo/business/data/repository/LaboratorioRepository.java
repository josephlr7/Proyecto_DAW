package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    @Query("SELECT l FROM Laboratorio l " +
           "LEFT JOIN FETCH l.personal p " +
           "LEFT JOIN FETCH l.equipamientos e " +
           "LEFT JOIN FETCH l.consumibles c " +
           "WHERE l.id = :id")
    Optional<Laboratorio> encontrarPorIdConDetalles(@Param("id") Long id);

    @Query("""
            SELECT l
            FROM Laboratorio l
            WHERE (
                :escuela IS NULL
                OR LOWER(l.escuela) LIKE LOWER(CONCAT('%', :escuela, '%'))
            )
            AND (
                :facultad IS NULL
                OR LOWER(l.facultad) LIKE LOWER(CONCAT('%', :facultad, '%'))
            )
            """)
    org.springframework.data.domain.Page<Laboratorio> buscarLaboratorios(
            @Param("escuela") String escuela,
            @Param("facultad") String facultad,
            org.springframework.data.domain.Pageable pageable
    );
}

