package com.cibertec.tienda.business.api.controller;

import com.cibertec.tienda.business.api.dto.empleado.EmpleadoRequestDto;
import com.cibertec.tienda.business.api.dto.empleado.EmpleadoResponseDto;
import com.cibertec.tienda.business.domain.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(
            EmpleadoService empleadoService
    ) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(
                empleadoService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDto> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                empleadoService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDto> crear(
            @Valid @RequestBody EmpleadoRequestDto requestDto
    ) {
        EmpleadoResponseDto empleadoCreado =
                empleadoService.crear(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empleadoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                empleadoService.actualizar(id, requestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        empleadoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<EmpleadoResponseDto>
    buscarPorNumeroDocumento(
            @PathVariable String numeroDocumento
    ) {
        return ResponseEntity.ok(
                empleadoService.buscarPorNumeroDocumento(
                        numeroDocumento
                )
        );
    }

    @GetMapping("/consulta")
    public ResponseEntity<Page<EmpleadoResponseDto>> consultar(
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "cargo"
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                empleadoService.consultar(
                        cargo,
                        activo,
                        pageable
                )
        );
    }
}
