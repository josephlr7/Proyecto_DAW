package com.cibertec.tienda.business.api.controller;

import com.cibertec.tienda.business.api.dto.cliente.ClienteRequestDto;
import com.cibertec.tienda.business.api.dto.cliente.ClienteResponseDto;
import com.cibertec.tienda.business.domain.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                clienteService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> crear(
            @Valid @RequestBody ClienteRequestDto requestDto
    ) {
        ClienteResponseDto clienteCreado =
                clienteService.crear(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                clienteService.actualizar(id, requestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        clienteService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/documento/{numeroDocumento}")
    public ResponseEntity<ClienteResponseDto>
    buscarPorNumeroDocumento(
            @PathVariable String numeroDocumento
    ) {
        return ResponseEntity.ok(
                clienteService.buscarPorNumeroDocumento(
                        numeroDocumento
                )
        );
    }

    @GetMapping("/consulta")
    public ResponseEntity<Page<ClienteResponseDto>> consultar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "nombres"
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                clienteService.consultar(
                        nombre,
                        activo,
                        pageable
                )
        );
    }
}
