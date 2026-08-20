package com.example.demo.business.domain.service.impl;

import com.example.demo.business.api.dto.UsoConsumibleRequestDTO;
import com.example.demo.business.api.dto.UsoConsumibleResponseDTO;
import com.example.demo.business.api.dto.UsoEquipamientoRequestDTO;
import com.example.demo.business.api.dto.UsoEquipamientoResponseDTO;
import com.example.demo.business.data.entity.Consumible;
import com.example.demo.business.data.entity.Equipamiento;
import com.example.demo.business.data.entity.UsoConsumible;
import com.example.demo.business.data.entity.UsoEquipamiento;
import com.example.demo.business.security.data.entity.Usuario;
import com.example.demo.business.api.exception.StockInsuficienteException;
import com.example.demo.business.api.exception.RecursoNoEncontradoException;
import com.example.demo.business.domain.mapper.UsoLaboratorioMapper;
import com.example.demo.business.data.repository.ConsumibleRepository;
import com.example.demo.business.data.repository.EquipamientoRepository;
import com.example.demo.business.security.data.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final EquipamientoRepository equipamientoRepository;
    private final ConsumibleRepository consumibleRepository;
    private final UsoLaboratorioMapper usoMapper;

    @Override
    @Transactional
    public UsoEquipamientoResponseDTO registrarUsoEquipamiento(UsoEquipamientoRequestDTO request) {
        String usernameDni = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(usernameDni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con DNI: " + usernameDni));

        Equipamiento eq = equipamientoRepository.findById(request.equipamientoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipamiento no encontrado con id: " + request.equipamientoId()));

        if (eq.getHorasUso() < request.horasUso()) {
            throw new StockInsuficienteException("Horas disponibles insuficientes para el equipo");
        }

        eq.setHorasUso(eq.getHorasUso() - request.horasUso());
        equipamientoRepository.save(eq);

        UsoEquipamiento uso = new UsoEquipamiento();
        uso.setUsuario(usuario);
        uso.setEquipamiento(eq);
        uso.setNombreInvestigador(request.nombreInvestigador());
        uso.setTipoInvestigacion(request.tipoInvestigacion());
        uso.setActividadNombre(request.actividadNombre());
        uso.setHorasUso(request.horasUso());
        uso.setFecha(request.fecha());
        uso.setHora(request.hora());
        uso.setObservacion(request.observacion());

        UsoEquipamiento guardado = usoEquipamientoRepository.saveAndFlush(uso);
        return usoMapper.toEquipamientoResponse(guardado);
    }

    @Override
    @Transactional
    public UsoConsumibleResponseDTO registrarUsoConsumible(UsoConsumibleRequestDTO request) {
        String usernameDni = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(usernameDni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con DNI: " + usernameDni));

        Consumible con = consumibleRepository.findById(request.consumibleId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Consumible no encontrado con id: " + request.consumibleId()));

        if (con.getCantidad() < request.cantidad()) {
            throw new StockInsuficienteException("Stock insuficiente para el consumible");
        }

        con.setCantidad(con.getCantidad() - request.cantidad());
        consumibleRepository.save(con);

        UsoConsumible uso = new UsoConsumible();
        uso.setUsuario(usuario);
        uso.setConsumible(con);
        uso.setNombreInvestigador(request.nombreInvestigador());
        uso.setTipoInvestigacion(request.tipoInvestigacion());
        uso.setActividadNombre(request.actividadNombre());
        uso.setCantidad(request.cantidad());
        uso.setFecha(request.fecha());
        uso.setHora(request.hora());
        uso.setObservacion(request.observacion());

        UsoConsumible guardado = usoConsumibleRepository.save(uso);
        return usoMapper.toConsumibleResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<UsoEquipamientoResponseDTO> obtenerUsosEquipamiento() {
        return usoEquipamientoRepository.findAll().stream()
                .map(usoMapper::toEquipamientoResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<UsoConsumibleResponseDTO> obtenerUsosConsumible() {
        return usoConsumibleRepository.findAll().stream()
                .map(usoMapper::toConsumibleResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public UsoEquipamientoResponseDTO editarUsoEquipamiento(Long id, UsoEquipamientoRequestDTO request) {
        UsoEquipamiento uso = usoEquipamientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Uso de equipamiento no encontrado con id: " + id));

        Equipamiento eq = equipamientoRepository.findById(request.equipamientoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Equipamiento no encontrado con id: " + request.equipamientoId()));

        if (!uso.getEquipamiento().getId().equals(eq.getId())) {
            Equipamiento eqAntiguo = uso.getEquipamiento();
            eqAntiguo.setHorasUso(eqAntiguo.getHorasUso() + uso.getHorasUso()); // Se restauran las horas
            equipamientoRepository.save(eqAntiguo);

            if (eq.getHorasUso() < request.horasUso()) {
                throw new StockInsuficienteException("Horas disponibles insuficientes para el nuevo equipo");
            }
            eq.setHorasUso(eq.getHorasUso() - request.horasUso()); // Se descuentan del nuevo equipo
            equipamientoRepository.save(eq);
        } else {
            double horasDiferencia = request.horasUso() - uso.getHorasUso();
            if (eq.getHorasUso() < horasDiferencia) {
                throw new StockInsuficienteException("Horas disponibles insuficientes para actualizar el uso");
            }
            eq.setHorasUso(eq.getHorasUso() - horasDiferencia); // Se descuenta la diferencia
            equipamientoRepository.save(eq);
        }

        uso.setEquipamiento(eq);
        uso.setNombreInvestigador(request.nombreInvestigador());
        uso.setTipoInvestigacion(request.tipoInvestigacion());
        uso.setActividadNombre(request.actividadNombre());
        uso.setHorasUso(request.horasUso());
        uso.setFecha(request.fecha());
        uso.setHora(request.hora());
        uso.setObservacion(request.observacion());

        UsoEquipamiento guardado = usoEquipamientoRepository.saveAndFlush(uso);
        return usoMapper.toEquipamientoResponse(guardado);
    }

    @Override
    @Transactional
    public UsoConsumibleResponseDTO editarUsoConsumible(Long id, UsoConsumibleRequestDTO request) {
        UsoConsumible uso = usoConsumibleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Uso de consumible no encontrado con id: " + id));

        Consumible con = consumibleRepository.findById(request.consumibleId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Consumible no encontrado con id: " + request.consumibleId()));

        if (!uso.getConsumible().getId().equals(con.getId())) {
            Consumible conAntiguo = uso.getConsumible();
            conAntiguo.setCantidad(conAntiguo.getCantidad() + uso.getCantidad());
            consumibleRepository.save(conAntiguo);

            if (con.getCantidad() < request.cantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para el nuevo consumible");
            }
            con.setCantidad(con.getCantidad() - request.cantidad());
            consumibleRepository.save(con);
        } else {
            double stockDiferencia = request.cantidad() - uso.getCantidad();
            if (con.getCantidad() < stockDiferencia) {
                throw new StockInsuficienteException("Stock insuficiente para actualizar el uso");
            }
            con.setCantidad(con.getCantidad() - stockDiferencia);
            consumibleRepository.save(con);
        }

        uso.setConsumible(con);
        uso.setNombreInvestigador(request.nombreInvestigador());
        uso.setTipoInvestigacion(request.tipoInvestigacion());
        uso.setActividadNombre(request.actividadNombre());
        uso.setCantidad(request.cantidad());
        uso.setFecha(request.fecha());
        uso.setHora(request.hora());
        uso.setObservacion(request.observacion());

        UsoConsumible guardado = usoConsumibleRepository.saveAndFlush(uso);
        return usoMapper.toConsumibleResponse(guardado);
    }

    @Override
    @Transactional
    public void eliminarUsoEquipamiento(Long id) {
        UsoEquipamiento uso = usoEquipamientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Uso de equipamiento no encontrado con id: " + id));
        Equipamiento eq = uso.getEquipamiento();
        eq.setHorasUso(eq.getHorasUso() + uso.getHorasUso()); // Restauramos las horas descontadas
        equipamientoRepository.save(eq);
        usoEquipamientoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarUsoConsumible(Long id) {
        UsoConsumible uso = usoConsumibleRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Uso de consumible no encontrado con id: " + id));
        Consumible con = uso.getConsumible();
        con.setCantidad(con.getCantidad() + uso.getCantidad()); // Restauramos la cantidad descontada
        consumibleRepository.save(con);
        usoConsumibleRepository.deleteById(id);
    }
}
