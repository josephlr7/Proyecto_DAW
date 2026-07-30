package com.example.demo.repository;

import com.example.demo.entity.Consumible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConsumibleRepository extends JpaRepository<Consumible, Long> {

    @Query("SELECT c FROM Consumible c WHERE c.cantidad <= c.stockMinimo")
    List<Consumible> buscarConsumiblesStockCritico();

    @Query("SELECT c FROM Consumible c WHERE c.laboratorio.id = :laboratorioId")
    List<Consumible> buscarPorLaboratorioId(@Param("laboratorioId") Long laboratorioId);
}
