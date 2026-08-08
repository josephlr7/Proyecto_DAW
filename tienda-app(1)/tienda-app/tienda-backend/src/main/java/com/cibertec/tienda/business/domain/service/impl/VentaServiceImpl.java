package com.cibertec.tienda.business.domain.service.impl;

import com.cibertec.tienda.business.api.dto.venta.DetalleVentaRequestDto;
import com.cibertec.tienda.business.api.dto.venta.VentaRequestDto;
import com.cibertec.tienda.business.api.dto.venta.VentaResponseDto;
import com.cibertec.tienda.business.api.exception.RecursoNoEncontradoException;
import com.cibertec.tienda.business.api.exception.SolicitudInvalidaException;
import com.cibertec.tienda.business.data.entity.*;
import com.cibertec.tienda.business.data.entity.enums.EstadoVenta;
import com.cibertec.tienda.business.data.repository.ClienteRepository;
import com.cibertec.tienda.business.data.repository.EmpleadoRepository;
import com.cibertec.tienda.business.data.repository.ProductoRepository;
import com.cibertec.tienda.business.data.repository.VentaRepository;
import com.cibertec.tienda.business.domain.mapper.VentaMapper;
import com.cibertec.tienda.business.domain.service.VentaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper ventaMapper;

    public VentaServiceImpl(
            VentaRepository ventaRepository,
            ClienteRepository clienteRepository,
            EmpleadoRepository empleadoRepository,
            ProductoRepository productoRepository,
            VentaMapper ventaMapper
    ) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.productoRepository = productoRepository;
        this.ventaMapper = ventaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDto> obtenerTodas() {
        return ventaRepository.findAll()
                .stream()
                .map(ventaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponseDto obtenerPorId(Long id) {
        Venta venta = buscarVentaCompleta(id);

        return ventaMapper.toResponseDto(venta);
    }

    @Override
    public VentaResponseDto registrar(
            VentaRequestDto requestDto
    ) {
        Cliente cliente = buscarClienteActivo(
                requestDto.clienteId()
        );

        Empleado empleado = buscarEmpleadoActivo(
                requestDto.empleadoId()
        );

        validarProductosDuplicados(
                requestDto.detalles()
        );

        Venta venta = new Venta();

        venta.setCliente(cliente);
        venta.setEmpleado(empleado);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setMetodoPago(requestDto.metodoPago());
        venta.setEstado(EstadoVenta.REGISTRADA);

        BigDecimal subtotalVenta = BigDecimal.ZERO;

        for (DetalleVentaRequestDto detalleDto
                : requestDto.detalles()) {

            DetalleVenta detalle =
                    construirDetalle(detalleDto);

            subtotalVenta = subtotalVenta.add(
                    detalle.getSubtotal()
            );

            venta.agregarDetalle(detalle);
        }

        BigDecimal descuentoVenta =
                obtenerDescuento(requestDto.descuento());

        if (descuentoVenta.compareTo(subtotalVenta) > 0) {
            throw new SolicitudInvalidaException(
                    "El descuento de la venta no puede ser "
                            + "mayor que el subtotal"
            );
        }

        BigDecimal total =
                subtotalVenta.subtract(descuentoVenta);

        venta.setSubtotal(subtotalVenta);
        venta.setDescuento(descuentoVenta);
        venta.setTotal(total);

        Venta ventaGuardada =
                ventaRepository.save(venta);

        return ventaMapper.toResponseDto(ventaGuardada);
    }

    @Override
    public void anular(Long id) {
        Venta venta = buscarVentaCompleta(id);

        if (venta.getEstado() == EstadoVenta.ANULADA) {
            throw new SolicitudInvalidaException(
                    "La venta ya se encuentra anulada"
            );
        }

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();

            producto.setStock(
                    producto.getStock()
                            + detalle.getCantidad()
            );

            producto.setFechaActualizacion(
                    LocalDateTime.now()
            );
        }

        venta.setEstado(EstadoVenta.ANULADA);

        ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaResponseDto> obtenerPorCliente(
            Long clienteId,
            Pageable pageable
    ) {
        return ventaRepository
                .findByClienteId(clienteId, pageable)
                .map(ventaMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaResponseDto> obtenerPorEmpleado(
            Long empleadoId,
            Pageable pageable
    ) {
        return ventaRepository
                .findByEmpleadoId(empleadoId, pageable)
                .map(ventaMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaResponseDto> obtenerPorEstado(
            EstadoVenta estado,
            Pageable pageable
    ) {
        return ventaRepository
                .findByEstado(estado, pageable)
                .map(ventaMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VentaResponseDto> obtenerEntreFechas(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable
    ) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new SolicitudInvalidaException(
                    "La fecha inicial no puede ser posterior "
                            + "a la fecha final"
            );
        }

        return ventaRepository
                .findByFechaVentaBetween(
                        fechaInicio,
                        fechaFin,
                        pageable
                )
                .map(ventaMapper::toResponseDto);
    }

    private DetalleVenta construirDetalle(
            DetalleVentaRequestDto detalleDto
    ) {
        Producto producto = productoRepository
                .findById(detalleDto.productoId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el producto con id: "
                                        + detalleDto.productoId()
                        )
                );

        if (!producto.isActivo()) {
            throw new SolicitudInvalidaException(
                    "El producto no está activo: "
                            + producto.getNombre()
            );
        }

        if (producto.getStock() < detalleDto.cantidad()) {
            throw new SolicitudInvalidaException(
                    "Stock insuficiente para el producto: "
                            + producto.getNombre()
            );
        }

        BigDecimal precioUnitario =
                obtenerPrecioActual(producto);

        BigDecimal importeBruto =
                precioUnitario.multiply(
                        BigDecimal.valueOf(
                                detalleDto.cantidad()
                        )
                );

        BigDecimal descuentoDetalle =
                obtenerDescuento(
                        detalleDto.descuento()
                );

        if (descuentoDetalle.compareTo(importeBruto) > 0) {
            throw new SolicitudInvalidaException(
                    "El descuento no puede ser mayor que "
                            + "el importe del producto: "
                            + producto.getNombre()
            );
        }

        BigDecimal subtotalDetalle =
                importeBruto.subtract(
                        descuentoDetalle
                );

        DetalleVenta detalle = new DetalleVenta();

        detalle.setProducto(producto);
        detalle.setCantidad(detalleDto.cantidad());
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setDescuento(descuentoDetalle);
        detalle.setSubtotal(subtotalDetalle);

        producto.setStock(
                producto.getStock()
                        - detalleDto.cantidad()
        );

        producto.setFechaActualizacion(
                LocalDateTime.now()
        );

        return detalle;
    }

    private Cliente buscarClienteActivo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el cliente con id: "
                                        + id
                        )
                );

        if (!cliente.isActivo()) {
            throw new SolicitudInvalidaException(
                    "El cliente no está activo"
            );
        }

        return cliente;
    }

    private Empleado buscarEmpleadoActivo(Long id) {
        Empleado empleado =
                empleadoRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "No se encontró el empleado con id: "
                                                + id
                                )
                        );

        if (!empleado.isActivo()) {
            throw new SolicitudInvalidaException(
                    "El empleado no está activo"
            );
        }

        return empleado;
    }

    private Venta buscarVentaCompleta(Long id) {
        return ventaRepository.findOneById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró la venta con id: "
                                        + id
                        )
                );
    }

    private BigDecimal obtenerPrecioActual(
            Producto producto
    ) {
        if (producto.getPrecioOferta() != null) {
            return producto.getPrecioOferta();
        }

        return producto.getPrecio();
    }

    private BigDecimal obtenerDescuento(
            BigDecimal descuento
    ) {
        return descuento == null
                ? BigDecimal.ZERO
                : descuento;
    }

    private void validarProductosDuplicados(
            List<DetalleVentaRequestDto> detalles
    ) {
        Set<Long> productos = new HashSet<>();

        for (DetalleVentaRequestDto detalle : detalles) {
            if (!productos.add(detalle.productoId())) {
                throw new SolicitudInvalidaException(
                        "El producto con id "
                                + detalle.productoId()
                                + " está repetido en la venta"
                );
            }
        }
    }
}
