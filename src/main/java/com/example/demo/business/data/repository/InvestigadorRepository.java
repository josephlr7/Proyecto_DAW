package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.Investigador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestigadorRepository extends JpaRepository<Investigador, Long> {
    Optional<Investigador> findByDni(String dni);
}

