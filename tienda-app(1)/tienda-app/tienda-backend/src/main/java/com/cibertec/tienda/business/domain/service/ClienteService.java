package com.cibertec.tienda.business.domain.service;

import com.cibertec.tienda.business.api.dto.cliente.ClienteRequestDto;
import com.cibertec.tienda.business.api.dto.cliente.ClienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {

    List<ClienteResponseDto> obtenerTodos();

    ClienteResponseDto obtenerPorId(Long id);

    ClienteResponseDto crear(ClienteRequestDto requestDto);

    ClienteResponseDto actualizar(Long id, ClienteRequestDto requestDto);

    void eliminar(Long id);

    ClienteResponseDto buscarPorNumeroDocumento(String numeroDocumento);

    Page<ClienteResponseDto> consultar(
            String nombre,
            Boolean activo,
            Pageable pageable
    );
}
