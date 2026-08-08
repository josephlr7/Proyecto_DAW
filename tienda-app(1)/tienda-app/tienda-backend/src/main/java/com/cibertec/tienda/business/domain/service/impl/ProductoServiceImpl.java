package com.cibertec.tienda.business.domain.service.impl;

import com.cibertec.tienda.business.api.dto.producto.ProductoRequestDto;
import com.cibertec.tienda.business.api.dto.producto.ProductoResponseDto;
import com.cibertec.tienda.business.api.exception.RecursoDuplicadoException;
import com.cibertec.tienda.business.api.exception.RecursoNoEncontradoException;
import com.cibertec.tienda.business.api.exception.SolicitudInvalidaException;
import com.cibertec.tienda.business.data.entity.Producto;
import com.cibertec.tienda.business.data.repository.ProductoRepository;
import com.cibertec.tienda.business.domain.mapper.ProductoMapper;
import com.cibertec.tienda.business.domain.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private static final Set<String> CAMPOS_ORDEN =
            Set.of("nombre", "precio", "stock");

    private static final Set<String> DIRECCIONES =
            Set.of("asc", "desc");

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            ProductoMapper productoMapper
    ) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerPorId(Long id) {
        Producto producto = buscarProductoPorId(id);

        return productoMapper.toResponseDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto buscarPorSku(String sku) {
        String skuNormalizado = normalizarSku(sku);

        Producto producto = productoRepository
                .findBySku(skuNormalizado)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el producto con SKU: "
                                        + skuNormalizado
                        )
                );

        return productoMapper.toResponseDto(producto);
    }

    @Override
    public ProductoResponseDto crear(
            ProductoRequestDto requestDto
    ) {
        validarPrecios(requestDto);

        String skuNormalizado =
                normalizarSku(requestDto.sku());

        if (productoRepository.existsBySku(skuNormalizado)) {
            throw new RecursoDuplicadoException(
                    "El SKU ya está registrado"
            );
        }

        Producto producto =
                productoMapper.toEntity(requestDto);

        normalizarProducto(producto, skuNormalizado);

        LocalDateTime ahora = LocalDateTime.now();

        producto.setActivo(true);
        producto.setFechaCreacion(ahora);
        producto.setFechaActualizacion(ahora);

        Producto productoGuardado =
                productoRepository.save(producto);

        return productoMapper.toResponseDto(productoGuardado);
    }

    @Override
    public ProductoResponseDto actualizar(
            Long id,
            ProductoRequestDto requestDto
    ) {
        Producto producto = buscarProductoPorId(id);

        validarPrecios(requestDto);

        String skuNormalizado =
                normalizarSku(requestDto.sku());

        if (productoRepository.existsBySkuAndIdNot(
                skuNormalizado,
                id
        )) {
            throw new RecursoDuplicadoException(
                    "El SKU ya está registrado"
            );
        }

        productoMapper.actualizarEntidad(
                requestDto,
                producto
        );

        normalizarProducto(producto, skuNormalizado);

        producto.setFechaActualizacion(
                LocalDateTime.now()
        );

        Producto productoActualizado =
                productoRepository.save(producto);

        return productoMapper.toResponseDto(
                productoActualizado
        );
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = buscarProductoPorId(id);

        producto.setActivo(false);
        producto.setFechaActualizacion(
                LocalDateTime.now()
        );

        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> consultar(
            String nombre,
            String marca,
            String categoria,
            Boolean activo,
            BigDecimal precioMinimo,
            BigDecimal precioMaximo,
            String orden,
            String direccion
    ) {
        validarRangoPrecios(
                precioMinimo,
                precioMaximo
        );

        String ordenNormalizado =
                normalizarOrden(orden);

        String direccionNormalizada =
                normalizarDireccion(direccion);

        return productoRepository
                .consultar(
                        normalizarTexto(nombre),
                        normalizarTexto(marca),
                        normalizarTexto(categoria),
                        activo,
                        precioMinimo,
                        precioMaximo,
                        ordenNormalizado,
                        direccionNormalizada
                )
                .stream()
                .map(productoMapper::toResponseDto)
                .toList();
    }

    private Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No se encontró el producto con id: "
                                        + id
                        )
                );
    }

    private void validarPrecios(
            ProductoRequestDto requestDto
    ) {
        BigDecimal precioOferta =
                requestDto.precioOferta();

        if (precioOferta != null
                && precioOferta.compareTo(
                requestDto.precio()
        ) > 0) {
            throw new SolicitudInvalidaException(
                    "El precio de oferta no puede ser mayor "
                            + "que el precio regular"
            );
        }
    }

    private void validarRangoPrecios(
            BigDecimal precioMinimo,
            BigDecimal precioMaximo
    ) {
        if (precioMinimo != null
                && precioMinimo.signum() < 0) {
            throw new SolicitudInvalidaException(
                    "El precio mínimo no puede ser negativo"
            );
        }

        if (precioMaximo != null
                && precioMaximo.signum() < 0) {
            throw new SolicitudInvalidaException(
                    "El precio máximo no puede ser negativo"
            );
        }

        if (precioMinimo != null
                && precioMaximo != null
                && precioMinimo.compareTo(precioMaximo) > 0) {
            throw new SolicitudInvalidaException(
                    "El precio mínimo no puede ser mayor "
                            + "que el precio máximo"
            );
        }
    }

    private void normalizarProducto(
            Producto producto,
            String skuNormalizado
    ) {
        producto.setNombre(
                producto.getNombre().trim()
        );
        producto.setSku(skuNormalizado);
        producto.setMarca(
                producto.getMarca().trim()
        );
        producto.setCategoria(
                producto.getCategoria().trim()
        );
    }

    private String normalizarSku(String sku) {
        return sku.trim().toUpperCase();
    }

    private String normalizarTexto(String texto) {
        return texto == null || texto.isBlank()
                ? null
                : texto.trim();
    }

    private String normalizarOrden(String orden) {
        if (orden == null || orden.isBlank()) {
            return "nombre";
        }

        String valor = orden.trim().toLowerCase();

        if (!CAMPOS_ORDEN.contains(valor)) {
            throw new SolicitudInvalidaException(
                    "El orden debe ser: nombre, precio o stock"
            );
        }

        return valor;
    }

    private String normalizarDireccion(
            String direccion
    ) {
        if (direccion == null || direccion.isBlank()) {
            return "asc";
        }

        String valor =
                direccion.trim().toLowerCase();

        if (!DIRECCIONES.contains(valor)) {
            throw new SolicitudInvalidaException(
                    "La dirección debe ser asc o desc"
            );
        }

        return valor;
    }
}
