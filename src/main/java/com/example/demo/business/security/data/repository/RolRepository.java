package com.example.demo.business.security.data.repository;

import com.example.demo.business.security.data.entity.NombreRol;
import com.example.demo.business.security.data.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(
            NombreRol nombre
    );
}
