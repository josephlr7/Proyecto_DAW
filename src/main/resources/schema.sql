-- ==========================================
-- SCRIPT DE BASE DE DATOS FISICO (MySQL)
-- SISTEMA DE GESTION DE LABORATORIOS UNT
-- ==========================================

-- 1. Tabla de Perfiles de Personal
CREATE TABLE IF NOT EXISTS perfiles_personal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_contratacion DATE,
    biografia VARCHAR(500),
    nro_oficina VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Tabla Base: Personas (Herencia JOINED)
CREATE TABLE IF NOT EXISTS personas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    genero VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tabla de Laboratorios
CREATE TABLE IF NOT EXISTS laboratorios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    facultad VARCHAR(100) NOT NULL,
    escuela VARCHAR(100) NOT NULL,
    area_investigacion VARCHAR(100) NOT NULL,
    lineas_investigacion VARCHAR(500) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    resolucion_numero VARCHAR(100),
    correo_institucional VARCHAR(100) NOT NULL UNIQUE,
    ods VARCHAR(100),
    posee_sistema_gestion BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Tabla Derivada: Personal de Laboratorio
CREATE TABLE IF NOT EXISTS personal_laboratorio (
    personal_id BIGINT PRIMARY KEY,
    resolucion_numero VARCHAR(100),
    cargo VARCHAR(50) NOT NULL,
    foto_url VARCHAR(255),
    es_docente BOOLEAN NOT NULL DEFAULT FALSE,
    renacyt BOOLEAN NOT NULL DEFAULT FALSE,
    es_docente_investigador_unt BOOLEAN NOT NULL DEFAULT FALSE,
    condicion VARCHAR(50) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    perfil_id BIGINT NULL,
    laboratorio_id BIGINT NULL,
    CONSTRAINT fk_personal_persona FOREIGN KEY (personal_id) 
        REFERENCES personas(id) ON DELETE CASCADE,
    CONSTRAINT fk_personal_perfil FOREIGN KEY (perfil_id) 
        REFERENCES perfiles_personal(id) ON DELETE SET NULL,
    CONSTRAINT fk_personal_laboratorio FOREIGN KEY (laboratorio_id) 
        REFERENCES laboratorios(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Tabla Derivada: Investigadores
CREATE TABLE IF NOT EXISTS investigadores (
    investigador_id BIGINT PRIMARY KEY,
    programa_estudios VARCHAR(100) NOT NULL,
    grado_academico VARCHAR(50) NOT NULL,
    CONSTRAINT fk_investigador_persona FOREIGN KEY (investigador_id) 
        REFERENCES personas(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Tabla de Equipamientos
CREATE TABLE IF NOT EXISTS equipamientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    funcion VARCHAR(255) NOT NULL,
    rango_precio VARCHAR(50) NOT NULL,
    ano_adquisicion INT NOT NULL,
    horas_uso DOUBLE NOT NULL DEFAULT 0.0,
    estado VARCHAR(20) NOT NULL,
    programa_mantenimiento VARCHAR(50) NOT NULL,
    programa_mantenimiento_horas INT,
    se_cumple_mantenimiento VARCHAR(20),
    requiere_consumible BOOLEAN NOT NULL DEFAULT FALSE,
    tipo_consumible_requerido VARCHAR(50),
    laboratorio_id BIGINT NOT NULL,
    CONSTRAINT fk_equipamientos_laboratorio FOREIGN KEY (laboratorio_id) 
        REFERENCES laboratorios(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Tabla de Consumibles
CREATE TABLE IF NOT EXISTS consumibles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    empresa VARCHAR(100) NOT NULL,
    estado_adquirido VARCHAR(20) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    unidad_medida VARCHAR(50) NOT NULL,
    funcion VARCHAR(255),
    rango_precio VARCHAR(50) NOT NULL,
    fecha_adquisicion DATE,
    fecha_vencimiento DATE,
    cantidad DOUBLE NOT NULL DEFAULT 0.0,
    stock_minimo DOUBLE NOT NULL DEFAULT 0.0,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    laboratorio_id BIGINT NOT NULL,
    CONSTRAINT fk_consumibles_laboratorio FOREIGN KEY (laboratorio_id) 
        REFERENCES laboratorios(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Tabla de Usos de Equipamiento
CREATE TABLE IF NOT EXISTS usos_equipamiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    investigador_id BIGINT NOT NULL,
    equipamiento_id BIGINT NOT NULL,
    tipo_investigacion VARCHAR(100) NOT NULL,
    actividad_nombre VARCHAR(150) NOT NULL,
    horas_uso DOUBLE NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    observacion VARCHAR(255),
    CONSTRAINT fk_usos_eq_investigador FOREIGN KEY (investigador_id) 
        REFERENCES investigadores(investigador_id) ON DELETE RESTRICT,
    CONSTRAINT fk_usos_eq_equipamiento FOREIGN KEY (equipamiento_id) 
        REFERENCES equipamientos(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. Tabla de Usos de Consumibles
CREATE TABLE IF NOT EXISTS usos_consumible (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    investigador_id BIGINT NOT NULL,
    consumible_id BIGINT NOT NULL,
    tipo_investigacion VARCHAR(100) NOT NULL,
    actividad_nombre VARCHAR(150) NOT NULL,
    cantidad DOUBLE NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    observacion VARCHAR(255),
    CONSTRAINT fk_usos_con_investigador FOREIGN KEY (investigador_id) 
        REFERENCES investigadores(investigador_id) ON DELETE RESTRICT,
    CONSTRAINT fk_usos_con_consumible FOREIGN KEY (consumible_id) 
        REFERENCES consumibles(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. Tabla de Usuarios (Seguridad)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices físicos para optimización
CREATE INDEX idx_personas_dni ON personas(dni);
CREATE INDEX idx_personal_lab ON personal_laboratorio(laboratorio_id);
CREATE INDEX idx_equip_lab ON equipamientos(laboratorio_id);
CREATE INDEX idx_consum_lab ON consumibles(laboratorio_id);
CREATE INDEX idx_usos_eq_equip ON usos_equipamiento(equipamiento_id);
CREATE INDEX idx_usos_con_consum ON usos_consumible(consumible_id);
CREATE INDEX idx_usuarios_username ON usuarios(username);
