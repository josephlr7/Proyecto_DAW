package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.Investigador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestigadorRepository extends JpaRepository<Investigador, Long> {
}

