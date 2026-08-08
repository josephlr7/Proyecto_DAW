package com.example.demo.business.domain.service;

import com.example.demo.business.api.dto.PersonalLaboratorioRequestDTO;
import com.example.demo.business.api.dto.PersonalLaboratorioResponseDTO;
import java.util.List;

public interface PersonalLaboratorioService {
    PersonalLaboratorioResponseDTO registrarPersonal(PersonalLaboratorioRequestDTO request);
    PersonalLaboratorioResponseDTO actualizarPersonal(Long id, PersonalLaboratorioRequestDTO request);
    PersonalLaboratorioResponseDTO obtenerPorId(Long id);
    List<PersonalLaboratorioResponseDTO> obtenerTodos();
    List<PersonalLaboratorioResponseDTO> obtenerInvestigadoresRenacyt();
    void eliminarPersonal(Long id);
    org.springframework.data.domain.Page<PersonalLaboratorioResponseDTO> consultar(String cargo, String nombres, org.springframework.data.domain.Pageable pageable);
}

