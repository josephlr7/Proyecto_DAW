package com.cibertec.tienda.business.api.controller;

import com.cibertec.tienda.business.api.dto.venta.VentaRequestDto;
import com.cibertec.tienda.business.api.dto.venta.VentaResponseDto;
import com.cibertec.tienda.business.data.entity.enums.EstadoVenta;
import com.cibertec.tienda.business.domain.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDto>> obtenerTodas() {
        return ResponseEntity.ok(
                ventaService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDto> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ventaService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<VentaResponseDto> registrar(
            @Valid @RequestBody VentaRequestDto requestDto
    ) {
        VentaResponseDto ventaRegistrada =
                ventaService.registrar(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ventaRegistrada);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Void> anular(
            @PathVariable Long id
    ) {
        ventaService.anular(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<VentaResponseDto>> obtenerPorCliente(
            @PathVariable Long clienteId,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "fechaVenta"
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ventaService.obtenerPorCliente(
                        clienteId,
                        pageable
                )
        );
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<Page<VentaResponseDto>> obtenerPorEmpleado(
            @PathVariable Long empleadoId,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "fechaVenta"
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ventaService.obtenerPorEmpleado(
                        empleadoId,
                        pageable
                )
        );
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<VentaResponseDto>> obtenerPorEstado(
            @PathVariable EstadoVenta estado,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "fechaVenta"
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ventaService.obtenerPorEstado(
                        estado,
                        pageable
                )
        );
    }

    @GetMapping("/fechas")
    public ResponseEntity<Page<VentaResponseDto>> obtenerEntreFechas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin,

            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "fechaVenta"
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ventaService.obtenerEntreFechas(
                        fechaInicio,
                        fechaFin,
                        pageable
                )
        );
    }
}
