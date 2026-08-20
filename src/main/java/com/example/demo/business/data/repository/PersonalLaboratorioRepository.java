package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.PersonalLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface PersonalLaboratorioRepository extends JpaRepository<PersonalLaboratorio, Long> {

    java.util.Optional<PersonalLaboratorio> findByDni(String dni);

    @Query("SELECT p FROM PersonalLaboratorio p WHERE p.renacyt = true")
    List<PersonalLaboratorio> buscarInvestigadoresRenacytConPerfil();

    @Query("""
            SELECT p
            FROM PersonalLaboratorio p
            WHERE (
                :cargo IS NULL
                OR LOWER(p.cargo) LIKE LOWER(CONCAT('%', :cargo, '%'))
            )
            AND (
                :nombres IS NULL
                OR LOWER(p.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))
            )
            """)
    org.springframework.data.domain.Page<PersonalLaboratorio> buscarPersonal(
            @Param("cargo") String cargo,
            @Param("nombres") String nombres,
            org.springframework.data.domain.Pageable pageable
    );
}

