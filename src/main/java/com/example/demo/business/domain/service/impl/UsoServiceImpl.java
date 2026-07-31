package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.UsoConsumibleRequestDTO;
import com.example.demo.business.api.dto.UsoConsumibleResponseDTO;
import com.example.demo.business.api.dto.UsoEquipamientoRequestDTO;
import com.example.demo.business.api.dto.UsoEquipamientoResponseDTO;
import com.example.demo.business.data.entity.Consumible;
import com.example.demo.business.data.entity.Equipamiento;
import com.example.demo.business.data.entity.Investigador;
import com.example.demo.business.data.entity.UsoConsumible;
import com.example.demo.business.data.entity.UsoEquipamiento;
import com.example.demo.business.api.exception.StockInsuficienteException;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.domain.mapper.UsoLaboratorioMapper;
import com.example.demo.business.data.repository.ConsumibleRepository;
import com.example.demo.business.data.repository.EquipamientoRepository;
import com.example.demo.business.data.repository.InvestigadorRepository;
import com.example.demo.business.data.repository.UsoConsumibleRepository;
import com.example.demo.business.data.repository.UsoEquipamientoRepository;
import com.example.demo.business.domain.service.UsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsoServiceImpl implements UsoService {

    private final UsoEquipamientoRepository usoEquipamientoRepository;
    private final UsoConsumibleRepository usoConsumibleRepository;
    private final InvestigadorRepository investigadorRepository;
    private final EquipamientoRepository equipamientoRepository;
    private final ConsumibleRepository consumibleRepository;
    private final UsoLaboratorioMapper usoMapper;

    @Override
    @Transactional
    public UsoEquipamientoResponseDTO registrarUsoEquipamiento(UsoEquipamientoRequestDTO request) {
        Investigador inv = investigadorRepository.findById(request.investigadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Investigador no encontrado con id: " + request.investigadorId()));

        Equipamiento eq = equipamientoRepository.findById(request.equipamientoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipamiento no encontrado con id: " + request.equipamientoId()));

        // Actualizar horas de uso
        eq.setHorasUso(eq.getHorasUso() + request.horasUso());
        equipamientoRepository.save(eq);

        UsoEquipamiento uso = new UsoEquipamiento();
        uso.setInvestigador(inv);
        uso.setEquipamiento(eq);
        uso.setTipoInvestigacion(request.tipoInvestigacion());
        uso.setActividadNombre(request.actividadNombre());
        uso.setHorasUso(request.horasUso());
        uso.setFecha(request.fecha());
        uso.setHora(request.hora());
        uso.setObservacion(request.observacion());

        // Advanced Hibernate: Force flush to synchronize persistence context with DB immediately
        UsoEquipamiento guardado = usoEquipamientoRepository.saveAndFlush(uso);

        return usoMapper.toEquipamientoResponse(guardado);
    }

    @Override
    @Transactional
    public UsoConsumibleResponseDTO registrarUsoConsumible(UsoConsumibleRequestDTO request) {
        Investigador inv = investigadorRepository.findById(request.investigadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Investigador no encontrado con id: " + request.investigadorId()));

        Consumible con = consumibleRepository.findById(request.consumibleId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Consumible no encontrado con id: " + request.consumibleId()));

        if (con.getCantidad() < request.cantidad()) {
            throw new StockInsuficienteException("Stock insuficiente para el consumible: " + con.getNombre() +
                    ". Disponible: " + con.getCantidad() + " " + con.getUnidadMedida() + 
                    ", Solicitado: " + request.cantidad());
        }

        // Descontar cantidad
        con.setCantidad(con.getCantidad() - request.cantidad());
        consumibleRepository.save(con);

        UsoConsumible uso = new UsoConsumible();
        uso.setInvestigador(inv);
        uso.setConsumible(con);
        uso.setTipoInvestigacion(request.tipoInvestigacion());
        uso.setActividadNombre(request.actividadNombre());
        uso.setCantidad(request.cantidad());
        uso.setFecha(request.fecha());
        uso.setHora(request.hora());
        uso.setObservacion(request.observacion());

        // Advanced Hibernate: Force flush to synchronize persistence context with DB immediately
        UsoConsumible guardado = usoConsumibleRepository.saveAndFlush(uso);

        return usoMapper.toConsumibleResponse(guardado);
    }
}

