package com.cibertec.tienda.business.domain.service;

import com.cibertec.tienda.business.api.dto.producto.ProductoRequestDto;
import com.cibertec.tienda.business.api.dto.producto.ProductoResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    List<ProductoResponseDto> obtenerTodos();

    ProductoResponseDto obtenerPorId(Long id);

    ProductoResponseDto buscarPorSku(String sku);

    ProductoResponseDto crear(
            ProductoRequestDto requestDto
    );

    ProductoResponseDto actualizar(
            Long id,
            ProductoRequestDto requestDto
    );

    void eliminar(Long id);

    List<ProductoResponseDto> consultar(
            String nombre,
            String marca,
            String categoria,
            Boolean activo,
            BigDecimal precioMinimo,
            BigDecimal precioMaximo,
            String orden,
            String direccion
    );
}
