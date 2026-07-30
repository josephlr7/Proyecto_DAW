package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "investigadores")
@PrimaryKeyJoinColumn(name = "investigador_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Investigador extends Persona {

    @Column(name = "programa_estudios", nullable = false, length = 100)
    private String programaEstudios;

    @Column(name = "grado_academico", nullable = false, length = 50)
    private String gradoAcademico;
}
