package com.cibertec.tienda.business.domain.service;

import com.cibertec.tienda.business.api.dto.venta.VentaRequestDto;
import com.cibertec.tienda.business.api.dto.venta.VentaResponseDto;
import com.cibertec.tienda.business.data.entity.enums.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaService {

    List<VentaResponseDto> obtenerTodas();

    VentaResponseDto obtenerPorId(Long id);

    VentaResponseDto registrar(
            VentaRequestDto requestDto
    );

    void anular(Long id);

    Page<VentaResponseDto> obtenerPorCliente(
            Long clienteId,
            Pageable pageable
    );

    Page<VentaResponseDto> obtenerPorEmpleado(
            Long empleadoId,
            Pageable pageable
    );

    Page<VentaResponseDto> obtenerPorEstado(
            EstadoVenta estado,
            Pageable pageable
    );

    Page<VentaResponseDto> obtenerEntreFechas(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable
    );
}
