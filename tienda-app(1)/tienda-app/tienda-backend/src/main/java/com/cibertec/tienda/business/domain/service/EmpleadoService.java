package com.cibertec.tienda.business.domain.service;

import com.cibertec.tienda.business.api.dto.empleado.EmpleadoRequestDto;
import com.cibertec.tienda.business.api.dto.empleado.EmpleadoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpleadoService {

    List<EmpleadoResponseDto> obtenerTodos();

    EmpleadoResponseDto obtenerPorId(Long id);

    EmpleadoResponseDto crear(EmpleadoRequestDto requestDto);

    EmpleadoResponseDto actualizar(
            Long id,
            EmpleadoRequestDto requestDto
    );

    void eliminar(Long id);

    EmpleadoResponseDto buscarPorNumeroDocumento(
            String numeroDocumento
    );

    Page<EmpleadoResponseDto> consultar(
            String cargo,
            Boolean activo,
            Pageable pageable
    );
}
