package com.example.demo.business.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "perfiles_personal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerfilPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "biografia", length = 500)
    private String biografia;

    @Column(name = "nro_oficina", length = 20)
    private String nroOficina;

    @OneToOne(mappedBy = "perfil")
    private PersonalLaboratorio personal;
}

