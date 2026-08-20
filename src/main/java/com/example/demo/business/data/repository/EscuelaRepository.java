package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.Escuela;
import com.example.demo.business.data.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EscuelaRepository extends JpaRepository<Escuela, Long> {
    Optional<Escuela> findByNombreIgnoreCaseAndFacultad(String nombre, Facultad facultad);
    List<Escuela> findByFacultadId(Long facultadId);
}
