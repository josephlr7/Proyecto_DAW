package com.example.demo.repository;

import com.example.demo.entity.UsoEquipamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsoEquipamientoRepository extends JpaRepository<UsoEquipamiento, Long> {
}
