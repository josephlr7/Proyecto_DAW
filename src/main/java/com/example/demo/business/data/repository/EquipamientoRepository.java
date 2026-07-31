package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.Equipamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {

    @Query("SELECT e FROM Equipamiento e WHERE e.laboratorio.id = :laboratorioId")
    List<Equipamiento> buscarPorLaboratorioId(@Param("laboratorioId") Long laboratorioId);
}

