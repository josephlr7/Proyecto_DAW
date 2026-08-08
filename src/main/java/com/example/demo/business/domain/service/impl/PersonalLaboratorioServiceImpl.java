package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.PersonalLaboratorioRequestDTO;
import com.example.demo.business.api.dto.PersonalLaboratorioResponseDTO;
import com.example.demo.business.data.entity.Laboratorio;
import com.example.demo.business.data.entity.PersonalLaboratorio;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.domain.mapper.PersonalLaboratorioMapper;
import com.example.demo.business.data.repository.LaboratorioRepository;
import com.example.demo.business.data.repository.PersonalLaboratorioRepository;
import com.example.demo.business.domain.service.PersonalLaboratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalLaboratorioServiceImpl implements PersonalLaboratorioService {

    private final PersonalLaboratorioRepository personalRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final PersonalLaboratorioMapper personalMapper;

    @Override
    @Transactional
    public PersonalLaboratorioResponseDTO registrarPersonal(PersonalLaboratorioRequestDTO request) {
        PersonalLaboratorio personal = personalMapper.toEntity(request);
        
        if (request.laboratorioId() != null) {
            Laboratorio lab = laboratorioRepository.findById(request.laboratorioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + request.laboratorioId()));
            personal.setLaboratorio(lab);
        }

        if (personal.getPerfil() != null) {
            personal.getPerfil().setPersonal(personal);
        }

        PersonalLaboratorio guardado = personalRepository.save(personal);
        return personalMapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public PersonalLaboratorioResponseDTO actualizarPersonal(Long id, PersonalLaboratorioRequestDTO request) {
        PersonalLaboratorio personal = personalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Personal no encontrado con id: " + id));

        personalMapper.updateEntityFromRequest(request, personal);

        if (request.laboratorioId() != null) {
            Laboratorio lab = laboratorioRepository.findById(request.laboratorioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Laboratorio no encontrado con id: " + request.laboratorioId()));
            personal.setLaboratorio(lab);
        } else {
            personal.setLaboratorio(null);
        }

        if (personal.getPerfil() != null) {
            personal.getPerfil().setPersonal(personal);
        }

        PersonalLaboratorio guardado = personalRepository.save(personal);
        return personalMapper.toResponse(guardado);
    }

    @Override
    public PersonalLaboratorioResponseDTO obtenerPorId(Long id) {
        PersonalLaboratorio personal = personalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Personal no encontrado con id: " + id));
        return personalMapper.toResponse(personal);
    }

    @Override
    public List<PersonalLaboratorioResponseDTO> obtenerTodos() {
        return personalRepository.findAll().stream()
                .map(personalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonalLaboratorioResponseDTO> obtenerInvestigadoresRenacyt() {
        return personalRepository.buscarInvestigadoresRenacytConPerfil().stream()
                .map(personalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<PersonalLaboratorioResponseDTO> consultar(String cargo, String nombres, org.springframework.data.domain.Pageable pageable) {
        String cargoNorm = (cargo == null || cargo.isBlank()) ? null : cargo.trim();
        String nombresNorm = (nombres == null || nombres.isBlank()) ? null : nombres.trim();
        return personalRepository.buscarPersonal(cargoNorm, nombresNorm, pageable)
                .map(personalMapper::toResponse);
    }

    @Override
    @Transactional
    public void eliminarPersonal(Long id) {
        PersonalLaboratorio personal = personalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Personal no encontrado con id: " + id));
        
        if (personal.getLaboratorio() != null) {
            personal.setActivo(false);
            personalRepository.save(personal);
        } else {
            personalRepository.delete(personal);
        }
    }
}

