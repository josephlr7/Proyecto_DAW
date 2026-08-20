package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.UsoEquipamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsoEquipamientoRepository extends JpaRepository<UsoEquipamiento, Long> {
    boolean existsByUsuarioUsername(String username);
}

