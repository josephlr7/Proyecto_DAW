package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.PersonalLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonalLaboratorioRepository extends JpaRepository<PersonalLaboratorio, Long> {

    @Query("SELECT p FROM PersonalLaboratorio p JOIN FETCH p.perfil WHERE p.renacyt = true")
    List<PersonalLaboratorio> buscarInvestigadoresRenacytConPerfil();
}

