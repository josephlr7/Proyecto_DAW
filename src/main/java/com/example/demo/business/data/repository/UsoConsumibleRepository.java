package com.example.demo.business.data.repository;

import com.example.demo.business.data.entity.UsoConsumible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsoConsumibleRepository extends JpaRepository<UsoConsumible, Long> {
    boolean existsByUsuarioUsername(String username);
}

