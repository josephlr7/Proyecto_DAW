package com.example.demo.service.impl;

import com.example.demo.dto.UsoConsumibleRequestDTO;
import com.example.demo.dto.UsoConsumibleResponseDTO;
import com.example.demo.dto.UsoEquipamientoRequestDTO;
import com.example.demo.dto.UsoEquipamientoResponseDTO;
import com.example.demo.entity.Consumible;
import com.example.demo.entity.Equipamiento;
import com.example.demo.entity.Investigador;
import com.example.demo.entity.UsoConsumible;
import com.example.demo.entity.UsoEquipamiento;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UsoLaboratorioMapper;
import com.example.demo.repository.ConsumibleRepository;
import com.example.demo.repository.EquipamientoRepository;
import com.example.demo.repository.InvestigadorRepository;
import com.example.demo.repository.UsoConsumibleRepository;
import com.example.demo.repository.UsoEquipamientoRepository;
import com.example.demo.service.UsoService;
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
                .orElseThrow(() -> new ResourceNotFoundException("Investigador no encontrado con id: " + request.investigadorId()));

        Equipamiento eq = equipamientoRepository.findById(request.equipamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipamiento no encontrado con id: " + request.equipamientoId()));

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
                .orElseThrow(() -> new ResourceNotFoundException("Investigador no encontrado con id: " + request.investigadorId()));

        Consumible con = consumibleRepository.findById(request.consumibleId())
                .orElseThrow(() -> new ResourceNotFoundException("Consumible no encontrado con id: " + request.consumibleId()));

        if (con.getCantidad() < request.cantidad()) {
            throw new InsufficientStockException("Stock insuficiente para el consumible: " + con.getNombre() +
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
