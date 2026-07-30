package com.example.demo.service;

import com.example.demo.dto.PersonalLaboratorioRequestDTO;
import com.example.demo.dto.PersonalLaboratorioResponseDTO;
import java.util.List;

public interface PersonalLaboratorioService {
    PersonalLaboratorioResponseDTO registrarPersonal(PersonalLaboratorioRequestDTO request);
    PersonalLaboratorioResponseDTO actualizarPersonal(Long id, PersonalLaboratorioRequestDTO request);
    PersonalLaboratorioResponseDTO obtenerPorId(Long id);
    List<PersonalLaboratorioResponseDTO> obtenerTodos();
    List<PersonalLaboratorioResponseDTO> obtenerInvestigadoresRenacyt();
    void eliminarPersonal(Long id);
}
