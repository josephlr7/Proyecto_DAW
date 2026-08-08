package com.cibertec.tienda.business.domain.service.impl;

import com.cibertec.tienda.business.api.dto.empleado.EmpleadoRequestDto;
import com.cibertec.tienda.business.api.dto.empleado.EmpleadoResponseDto;
import com.cibertec.tienda.business.api.exception.RecursoDuplicadoException;
import com.cibertec.tienda.business.api.exception.RecursoNoEncontradoException;
import com.cibertec.tienda.business.data.entity.Empleado;
import com.cibertec.tienda.business.data.repository.EmpleadoRepository;
import com.cibertec.tienda.business.domain.mapper.EmpleadoMapper;
import com.cibertec.tienda.business.domain.service.EmpleadoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    public EmpleadoServiceImpl(
            EmpleadoRepository empleadoRepository,
            EmpleadoMapper empleadoMapper
    ) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoMapper = empleadoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDto> obtenerTodos() {
        return empleadoRepository.findAll()
                .stream()
                .map(empleadoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDto obtenerPorId(Long id) {
        Empleado empleado = buscarEmpleadoPorId(id);

        return empleadoMapper.toResponseDto(empleado);
    }

    @Override
    public EmpleadoResponseDto crear(
            EmpleadoRequestDto requestDto
    ) {
        validarDuplicadosAlCrear(requestDto);

        Empleado empleado = empleadoMapper.toEntity(requestDto);

        empleado.setActivo(true);

        Empleado empleadoGuardado =
                empleadoRepository.save(empleado);

        return empleadoMapper.toResponseDto(empleadoGuardado);
    }

    @Override
    public EmpleadoResponseDto actualizar(
            Long id,
            EmpleadoRequestDto requestDto
    ) {
        Empleado empleado = buscarEmpleadoPorId(id);

        validarDuplicadosAlActualizar(id, requestDto);

        empleadoMapper.actualizarEntidad(requestDto, empleado);

        Empleado empleadoActualizado =
                empleadoRepository.save(empleado);

        return empleadoMapper.toResponseDto(empleadoActualizado);
    }

    @Override
    public void eliminar(Long id) {
        Empleado empleado = buscarEmpleadoPorId(id);

        empleado.setActivo(false);

        empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDto buscarPorNumeroDocumento(
            String numeroDocumento
    ) {
        Empleado empleado = empleadoRepository
                .findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró un empleado con el documento: "
                                        + numeroDocumento
                        )
                );

        return empleadoMapper.toResponseDto(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponseDto> consultar(
            String cargo,
            Boolean activo,
            Pageable pageable
    ) {
        String cargoNormalizado =
                cargo == null || cargo.isBlank()
                        ? null
                        : cargo.trim();

        return empleadoRepository
                .buscarEmpleados(
                        cargoNormalizado,
                        activo,
                        pageable
                )
                .map(empleadoMapper::toResponseDto);
    }

    private Empleado buscarEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el empleado con id: " + id
                        )
                );
    }

    private void validarDuplicadosAlCrear(
            EmpleadoRequestDto requestDto
    ) {
        if (empleadoRepository.existsByNumeroDocumento(
                requestDto.numeroDocumento()
        )) {
            throw new RecursoDuplicadoException(
                    "El número de documento ya está registrado"
            );
        }

        if (requestDto.email() != null
                && empleadoRepository.existsByEmail(
                requestDto.email()
        )) {
            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado"
            );
        }
    }

    private void validarDuplicadosAlActualizar(
            Long id,
            EmpleadoRequestDto requestDto
    ) {
        if (empleadoRepository.existsByNumeroDocumentoAndIdNot(
                requestDto.numeroDocumento(),
                id
        )) {
            throw new RecursoDuplicadoException(
                    "El número de documento ya está registrado"
            );
        }

        if (requestDto.email() != null
                && empleadoRepository.existsByEmailAndIdNot(
                requestDto.email(),
                id
        )) {
            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado"
            );
        }
    }
}
