package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.InvestigadorDTO;
import com.example.demo.business.data.entity.Investigador;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.data.repository.InvestigadorRepository;
import com.example.demo.business.data.repository.UsoEquipamientoRepository;
import com.example.demo.business.data.repository.UsoConsumibleRepository;
import com.example.demo.business.domain.service.InvestigadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvestigadorServiceImpl implements InvestigadorService {

    private final InvestigadorRepository investigadorRepository;
    private final UsoEquipamientoRepository usoEquipamientoRepository;
    private final UsoConsumibleRepository usoConsumibleRepository;

    @Override
    @Transactional
    public InvestigadorDTO registrarInvestigador(InvestigadorDTO dto) {
        Investigador inv = new Investigador();
        inv.setNombres(dto.nombres());
        inv.setApellidos(dto.apellidos());
        inv.setDni(dto.dni());
        inv.setGenero(dto.genero());
        inv.setProgramaEstudios(dto.programaEstudios());
        inv.setGradoAcademico(dto.gradoAcademico());
        Investigador guardado = investigadorRepository.save(inv);
        return toDto(guardado);
    }

    @Override
    public InvestigadorDTO obtenerPorId(Long id) {
        Investigador inv = investigadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Investigador no encontrado con id: " + id));
        return toDto(inv);
    }

    @Override
    public List<InvestigadorDTO> obtenerTodos() {
        return investigadorRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private InvestigadorDTO toDto(Investigador inv) {
        return new InvestigadorDTO(
                inv.getId(),
                inv.getNombres(),
                inv.getApellidos(),
                inv.getDni(),
                inv.getGenero(),
                inv.getProgramaEstudios(),
                inv.getGradoAcademico()
        );
    }

    @Override
    @Transactional
    public void eliminarInvestigador(Long id) {
        Investigador inv = investigadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Investigador no encontrado con id: " + id));

        boolean tieneUsos = usoEquipamientoRepository.existsByInvestigadorId(id) ||
                            usoConsumibleRepository.existsByInvestigadorId(id);

        if (tieneUsos) {
            inv.setActivo(false);
            investigadorRepository.save(inv);
        } else {
            investigadorRepository.delete(inv);
        }
    }
}

