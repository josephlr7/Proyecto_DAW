package com.cibertec.tienda.business.api.controller;

import com.cibertec.tienda.business.api.dto.producto.ProductoRequestDto;
import com.cibertec.tienda.business.api.dto.producto.ProductoResponseDto;
import com.cibertec.tienda.business.domain.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(
            ProductoService productoService
    ) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(
                productoService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                productoService.obtenerPorId(id)
        );
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductoResponseDto> buscarPorSku(
            @PathVariable String sku
    ) {
        return ResponseEntity.ok(
                productoService.buscarPorSku(sku)
        );
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDto> crear(
            @Valid @RequestBody ProductoRequestDto requestDto
    ) {
        ProductoResponseDto productoCreado =
                productoService.crear(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                productoService.actualizar(id, requestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consulta")
    public ResponseEntity<List<ProductoResponseDto>> consultar(
            @RequestParam(required = false)
            String nombre,

            @RequestParam(required = false)
            String marca,

            @RequestParam(required = false)
            String categoria,

            @RequestParam(required = false)
            Boolean activo,

            @RequestParam(required = false)
            BigDecimal precioMinimo,

            @RequestParam(required = false)
            BigDecimal precioMaximo,

            @RequestParam(defaultValue = "nombre")
            String orden,

            @RequestParam(defaultValue = "asc")
            String direccion
    ) {
        return ResponseEntity.ok(
                productoService.consultar(
                        nombre,
                        marca,
                        categoria,
                        activo,
                        precioMinimo,
                        precioMaximo,
                        orden,
                        direccion
                )
        );
    }
}
