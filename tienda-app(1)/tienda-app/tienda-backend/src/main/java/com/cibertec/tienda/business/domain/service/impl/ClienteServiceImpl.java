package com.cibertec.tienda.business.domain.service.impl;

import com.cibertec.tienda.business.api.dto.cliente.ClienteRequestDto;
import com.cibertec.tienda.business.api.dto.cliente.ClienteResponseDto;
import com.cibertec.tienda.business.api.exception.RecursoDuplicadoException;
import com.cibertec.tienda.business.api.exception.RecursoNoEncontradoException;
import com.cibertec.tienda.business.data.entity.Cliente;
import com.cibertec.tienda.business.data.repository.ClienteRepository;
import com.cibertec.tienda.business.domain.mapper.ClienteMapper;
import com.cibertec.tienda.business.domain.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(
            ClienteRepository clienteRepository,
            ClienteMapper clienteMapper
    ) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDto> obtenerTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPorId(Long id) {
        Cliente cliente = buscarClientePorId(id);

        return clienteMapper.toResponseDto(cliente);
    }

    @Override
    public ClienteResponseDto crear(ClienteRequestDto requestDto) {
        validarDuplicadosAlCrear(requestDto);

        Cliente cliente = clienteMapper.toEntity(requestDto);

        cliente.setActivo(true);
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaActualizacion(LocalDateTime.now());

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return clienteMapper.toResponseDto(clienteGuardado);
    }

    @Override
    public ClienteResponseDto actualizar(
            Long id,
            ClienteRequestDto requestDto
    ) {
        Cliente cliente = buscarClientePorId(id);

        validarDuplicadosAlActualizar(id, requestDto);

        clienteMapper.actualizarEntidad(requestDto, cliente);

        cliente.setFechaActualizacion(LocalDateTime.now());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return clienteMapper.toResponseDto(clienteActualizado);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = buscarClientePorId(id);

        cliente.setActivo(false);
        cliente.setFechaActualizacion(LocalDateTime.now());

        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto buscarPorNumeroDocumento(
            String numeroDocumento
    ) {
        Cliente cliente = clienteRepository
                .findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró un cliente con el documento: "
                                        + numeroDocumento
                        )
                );

        return clienteMapper.toResponseDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDto> consultar(
            String nombre,
            Boolean activo,
            Pageable pageable
    ) {
        String nombreNormalizado =
                nombre == null || nombre.isBlank()
                        ? null
                        : nombre.trim();

        return clienteRepository
                .buscarClientes(
                        nombreNormalizado,
                        activo,
                        pageable
                )
                .map(clienteMapper::toResponseDto);
    }

    private Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el cliente con id: " + id
                        )
                );
    }

    private void validarDuplicadosAlCrear(
            ClienteRequestDto requestDto
    ) {
        if (clienteRepository.existsByNumeroDocumento(
                requestDto.numeroDocumento()
        )) {
            throw new RecursoDuplicadoException(
                    "El número de documento ya está registrado"
            );
        }

        if (requestDto.email() != null
                && clienteRepository.existsByEmail(requestDto.email())) {
            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado"
            );
        }
    }

    private void validarDuplicadosAlActualizar(
            Long id,
            ClienteRequestDto requestDto
    ) {
        if (clienteRepository.existsByNumeroDocumentoAndIdNot(
                requestDto.numeroDocumento(),
                id
        )) {
            throw new RecursoDuplicadoException(
                    "El número de documento ya está registrado"
            );
        }

        if (requestDto.email() != null
                && clienteRepository.existsByEmailAndIdNot(
                requestDto.email(),
                id
        )) {
            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado"
            );
        }
    }
}
