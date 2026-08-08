-- =====================================================
-- Base de datos: tienda_dev
-- Proyecto: tienda
-- Ambiente: Desarrollo
-- Motor: MySQL
-- =====================================================

DROP DATABASE IF EXISTS tienda_dev;

CREATE DATABASE tienda_dev
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE tienda_dev;


CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(8) NOT NULL UNIQUE,
    email VARCHAR(150) UNIQUE,
    telefono VARCHAR(9),
    direccion VARCHAR(250),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME
);


CREATE TABLE empleados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(8) NOT NULL UNIQUE,
    email VARCHAR(150) UNIQUE,
    telefono VARCHAR(9),
    direccion VARCHAR(250),
    cargo VARCHAR(100) NOT NULL,
    sueldo DECIMAL(10, 2) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(150) NOT NULL,

    descripcion VARCHAR(500),

    sku VARCHAR(50) NOT NULL UNIQUE,

    marca VARCHAR(50) NOT NULL,

    categoria VARCHAR(50) NOT NULL,

    precio DECIMAL(10, 2) NOT NULL,

    precio_oferta DECIMAL(10, 2),

    stock INT NOT NULL DEFAULT 0,

    imagen_url VARCHAR(500),

    activo BOOLEAN NOT NULL DEFAULT TRUE,

    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    fecha_actualizacion DATETIME
        DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_producto_precio
        CHECK (precio > 0),

    CONSTRAINT chk_producto_precio_oferta
        CHECK (
            precio_oferta IS NULL
            OR (
                precio_oferta > 0
                AND precio_oferta <= precio
            )
        ),

    CONSTRAINT chk_producto_stock
        CHECK (stock >= 0)
);

INSERT INTO clientes (
    nombres,
    apellidos,
    numero_documento,
    email,
    telefono,
    direccion,
    activo,
    fecha_creacion
) VALUES
(
    'Juan',
    'Pérez Ramos',
    '12345678',
    'juan@gmail.com',
    '987654321',
    'Av. Lima 123',
    TRUE,
    NOW()
),
(
    'Ana',
    'Torres Ruiz',
    '87654321',
    'ana@gmail.com',
    '912345678',
    'Calle Los Olivos 456',
    TRUE,
    NOW()
);

INSERT INTO empleados (
    nombres,
    apellidos,
    numero_documento,
    email,
    telefono,
    direccion,
    cargo,
    sueldo,
    fecha_contratacion,
    activo
) VALUES
(
    'María',
    'López Díaz',
    '45678912',
    'maria@tienda.com',
    '923456789',
    'Av. Arequipa 500',
    'Cajera',
    1800.00,
    '2026-07-01',
    TRUE
),
(
    'Carlos',
    'Mendoza Flores',
    '78912345',
    'carlos@tienda.com',
    '934567891',
    'Calle Central 100',
    'Administrador',
    2800.00,
    '2026-06-15',
    TRUE
);

-- INSERT INTO PRODUCTOS

INSERT INTO productos (
    nombre,
    descripcion,
    sku,
    marca,
    categoria,
    precio,
    precio_oferta,
    stock,
    imagen_url,
    activo
) VALUES
(
    'iPad Wi-Fi 128 GB Plata (A16)',
    'iPad con chip A16, conexión Wi-Fi, almacenamiento de 128 GB y acabado color plata.',
    'IPAD-A16-128-PLATA',
    'Apple',
    'Tablets',
    1899.00,
    1699.00,
    15,
    '/images/productos/ipad-a16-plata.png',
    TRUE
),
(
    'iPhone 15 128 GB Negro',
    'iPhone 15 con almacenamiento de 128 GB y acabado color negro.',
    'IPHONE15-128-NEGRO',
    'Apple',
    'Smartphones',
    3599.00,
    2999.00,
    10,
    '/images/productos/iphone-15-negro.png',
    TRUE
),
(
    'iPhone 17 256 GB Blanco',
    'iPhone con almacenamiento de 256 GB y acabado color blanco.',
    'IPHONE17-256-BLANCO',
    'Apple',
    'Smartphones',
    4499.00,
    4099.00,
    8,
    '/images/productos/iphone-17-blanco.png',
    TRUE
);

SELECT * FROM clientes;
SELECT * FROM empleados;
SELECT * FROM productos;


DROP PROCEDURE IF EXISTS sp_producto_consultar;

DELIMITER $$

CREATE PROCEDURE sp_producto_consultar(
    IN p_nombre VARCHAR(150),
    IN p_marca VARCHAR(50),
    IN p_categoria VARCHAR(50),
    IN p_activo BOOLEAN,
    IN p_precio_minimo DECIMAL(10, 2),
    IN p_precio_maximo DECIMAL(10, 2),
    IN p_orden VARCHAR(20),
    IN p_direccion VARCHAR(4)
)
BEGIN
    SELECT
        id,
        nombre,
        descripcion,
        sku,
        marca,
        categoria,
        precio,
        precio_oferta,
        stock,
        imagen_url,
        activo,
        fecha_creacion,
        fecha_actualizacion
    FROM productos
    WHERE (
        p_nombre IS NULL
        OR TRIM(p_nombre) = ''
        OR LOWER(nombre)
            LIKE LOWER(CONCAT('%', TRIM(p_nombre), '%'))
    )
    AND (
        p_marca IS NULL
        OR TRIM(p_marca) = ''
        OR LOWER(marca) = LOWER(TRIM(p_marca))
    )
    AND (
        p_categoria IS NULL
        OR TRIM(p_categoria) = ''
        OR LOWER(categoria) = LOWER(TRIM(p_categoria))
    )
    AND (
        p_activo IS NULL
        OR activo = p_activo
    )
    AND (
        p_precio_minimo IS NULL
        OR COALESCE(precio_oferta, precio) >= p_precio_minimo
    )
    AND (
        p_precio_maximo IS NULL
        OR COALESCE(precio_oferta, precio) <= p_precio_maximo
    )
    ORDER BY
        CASE
            WHEN LOWER(p_orden) = 'nombre'
             AND LOWER(p_direccion) = 'asc'
            THEN nombre
        END ASC,

        CASE
            WHEN LOWER(p_orden) = 'nombre'
             AND LOWER(p_direccion) = 'desc'
            THEN nombre
        END DESC,

        CASE
            WHEN LOWER(p_orden) = 'precio'
             AND LOWER(p_direccion) = 'asc'
            THEN COALESCE(precio_oferta, precio)
        END ASC,

        CASE
            WHEN LOWER(p_orden) = 'precio'
             AND LOWER(p_direccion) = 'desc'
            THEN COALESCE(precio_oferta, precio)
        END DESC,

        CASE
            WHEN LOWER(p_orden) = 'stock'
             AND LOWER(p_direccion) = 'asc'
            THEN stock
        END ASC,

        CASE
            WHEN LOWER(p_orden) = 'stock'
             AND LOWER(p_direccion) = 'desc'
            THEN stock
        END DESC,

        nombre ASC;
END $$

DELIMITER ;

-- PROBAR SP

CALL sp_producto_consultar(
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    'nombre',
    'asc'
);

-- Productos cuyo nombre contiene iPhone:

CALL sp_producto_consultar(
    'iphone',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    'nombre',
    'asc'
);

-- Productos Apple activos:

CALL sp_producto_consultar(
    NULL,
    'Apple',
    NULL,
    TRUE,
    NULL,
    NULL,
    'nombre',
    'asc'
);

-- Productos de la categoría Smartphones:

CALL sp_producto_consultar(
    NULL,
    NULL,
    'Smartphones',
    TRUE,
    NULL,
    NULL,
    'precio',
    'asc'
);


-- TABLA VENTA

CREATE TABLE ventas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    cliente_id BIGINT NOT NULL,

    empleado_id BIGINT NOT NULL,

    fecha_venta DATETIME NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    subtotal DECIMAL(10, 2) NOT NULL,

    descuento DECIMAL(10, 2) NOT NULL
        DEFAULT 0.00,

    total DECIMAL(10, 2) NOT NULL,

    metodo_pago VARCHAR(30) NOT NULL,

    estado VARCHAR(20) NOT NULL
        DEFAULT 'REGISTRADA',

    CONSTRAINT fk_venta_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id),

    CONSTRAINT fk_venta_empleado
        FOREIGN KEY (empleado_id)
        REFERENCES empleados(id),

    CONSTRAINT chk_venta_subtotal
        CHECK (subtotal >= 0),

    CONSTRAINT chk_venta_descuento
        CHECK (descuento >= 0),

    CONSTRAINT chk_venta_total
        CHECK (total >= 0),

    CONSTRAINT chk_venta_descuento_subtotal
        CHECK (descuento <= subtotal),

    CONSTRAINT chk_venta_metodo_pago
        CHECK (
            metodo_pago IN (
                'EFECTIVO',
                'TARJETA',
                'TRANSFERENCIA',
                'YAPE',
                'PLIN'
            )
        ),

    CONSTRAINT chk_venta_estado
        CHECK (
            estado IN (
                'REGISTRADA',
                'ANULADA'
            )
        )
);

-- TABLA DETALLE VENTA

CREATE TABLE detalles_venta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    venta_id BIGINT NOT NULL,

    producto_id BIGINT NOT NULL,

    cantidad INT NOT NULL,

    precio_unitario DECIMAL(10, 2) NOT NULL,

    descuento DECIMAL(10, 2) NOT NULL
        DEFAULT 0.00,

    subtotal DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_detalle_venta
        FOREIGN KEY (venta_id)
        REFERENCES ventas(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id)
        REFERENCES productos(id),

    CONSTRAINT uk_detalle_venta_producto
        UNIQUE (venta_id, producto_id),

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_precio
        CHECK (precio_unitario > 0),

    CONSTRAINT chk_detalle_descuento
        CHECK (descuento >= 0),

    CONSTRAINT chk_detalle_subtotal
        CHECK (subtotal >= 0)
);

-- INSERT INTO VENTA

START TRANSACTION;

INSERT INTO ventas (
    cliente_id,
    empleado_id,
    subtotal,
    descuento,
    total,
    metodo_pago,
    estado
) VALUES (
    1,
    1,
    3398.00,
    100.00,
    3298.00,
    'TARJETA',
    'REGISTRADA'
);

SET @venta_id = LAST_INSERT_ID();

INSERT INTO detalles_venta (
    venta_id,
    producto_id,
    cantidad,
    precio_unitario,
    descuento,
    subtotal
) VALUES
(
    @venta_id,
    1,
    2,
    1699.00,
    100.00,
    3298.00
);

UPDATE productos
SET stock = stock - 2,
    fecha_actualizacion = NOW()
WHERE id = 1
  AND stock >= 2;

COMMIT;

-- CONSULTAR UNA VENTA COMPLETA

SELECT
    v.id AS venta_id,
    v.fecha_venta,
    CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
    CONCAT(e.nombres, ' ', e.apellidos) AS empleado,
    p.nombre AS producto,
    dv.cantidad,
    dv.precio_unitario,
    dv.descuento AS descuento_detalle,
    dv.subtotal AS subtotal_detalle,
    v.subtotal,
    v.descuento AS descuento_venta,
    v.total,
    v.metodo_pago,
    v.estado
FROM ventas v
INNER JOIN clientes c
    ON c.id = v.cliente_id
INNER JOIN empleados e
    ON e.id = v.empleado_id
INNER JOIN detalles_venta dv
    ON dv.venta_id = v.id
INNER JOIN productos p
    ON p.id = dv.producto_id
ORDER BY v.id, dv.id;

-- CONSULTAR DETALLES DE UNA VENTA

SELECT
    dv.id,
    dv.venta_id,
    p.sku,
    p.nombre AS producto,
    dv.cantidad,
    dv.precio_unitario,
    dv.descuento,
    dv.subtotal
FROM detalles_venta dv
INNER JOIN productos p
    ON p.id = dv.producto_id
WHERE dv.venta_id = 1;

-- Consultar ventas por cliente

SELECT
    v.id,
    v.fecha_venta,
    v.total,
    v.metodo_pago,
    v.estado
FROM ventas v
WHERE v.cliente_id = 1
ORDER BY v.fecha_venta DESC;



-- CONSULTA

SELECT * FROM ventas;
SELECT * FROM detalles_venta;















