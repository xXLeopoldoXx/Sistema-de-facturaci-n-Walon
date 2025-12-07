-- ============================================
-- SCRIPT DE BASE DE DATOS: FACTURACION_TIENDA
-- Sistema de Gestión y Facturación de Tienda
-- PostgreSQL Version
-- ============================================

-- Crear base de datos (ejecutar como superusuario postgres)
-- NOTA: Conectarse primero a la base de datos 'postgres' para crear esta BD
-- psql -U postgres
-- CREATE DATABASE facturacion_tienda;
-- \c facturacion_tienda

-- ============================================
-- TIPOS ENUMERADOS
-- ============================================
CREATE TYPE rol_usuario AS ENUM ('CAJERO', 'SUPERVISOR');
CREATE TYPE tipo_documento AS ENUM ('BOLETA', 'FACTURA');
CREATE TYPE tipo_venta AS ENUM ('MENOR', 'MAYOR');

-- ============================================
-- TABLA: usuarios
-- Almacena los usuarios del sistema (CAJERO, SUPERVISOR)
-- ============================================
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol rol_usuario NOT NULL DEFAULT 'CAJERO',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_username ON usuarios(username);

-- ============================================
-- TABLA: proveedores
-- Almacena información de los proveedores
-- ============================================
CREATE TABLE IF NOT EXISTS proveedores (
    id SERIAL PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    razon_social VARCHAR(200),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ruc ON proveedores(ruc);
CREATE INDEX IF NOT EXISTS idx_nombre ON proveedores(nombre);

-- ============================================
-- TABLA: clientes
-- Almacena información de los clientes
-- ============================================
CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_dni ON clientes(dni);
CREATE INDEX IF NOT EXISTS idx_nombre ON clientes(nombre);

-- ============================================
-- TABLA: productos
-- Almacena información de los productos
-- Relación con proveedores (FK)
-- ============================================
CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200) NOT NULL,
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    precio_unit DECIMAL(10, 2) NOT NULL CHECK (precio_unit >= 0),
    precio_mayor DECIMAL(10, 2) NOT NULL CHECK (precio_mayor >= 0),
    proveedor_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_codigo ON productos(codigo);
CREATE INDEX IF NOT EXISTS idx_proveedor ON productos(proveedor_id);
CREATE INDEX IF NOT EXISTS idx_stock ON productos(stock);

-- ============================================
-- TABLA: ventas
-- Almacena las cabeceras de las ventas
-- Relación con clientes (FK)
-- ============================================
CREATE TABLE IF NOT EXISTS ventas (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cliente_id INT NOT NULL,
    tipo_doc tipo_documento NOT NULL DEFAULT 'BOLETA',
    tipo_venta tipo_venta NOT NULL DEFAULT 'MENOR',
    delivery BOOLEAN NOT NULL DEFAULT FALSE,
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    igv DECIMAL(10, 2) NOT NULL CHECK (igv >= 0),
    total DECIMAL(10, 2) NOT NULL CHECK (total >= 0),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_cliente ON ventas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_fecha ON ventas(fecha);
CREATE INDEX IF NOT EXISTS idx_tipo_doc ON ventas(tipo_doc);

-- ============================================
-- TABLA: venta_detalle
-- Almacena los detalles de cada venta
-- Relación con ventas (FK) y productos (FK)
-- ============================================
CREATE TABLE IF NOT EXISTS venta_detalle (
    id SERIAL PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    importe DECIMAL(10, 2) NOT NULL CHECK (importe >= 0),
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_venta ON venta_detalle(venta_id);
CREATE INDEX IF NOT EXISTS idx_producto ON venta_detalle(producto_id);

-- ============================================
-- TABLA DE LOG (opcional)
-- ============================================
CREATE TABLE IF NOT EXISTS productos_log (
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL,
    stock_anterior INT,
    stock_nuevo INT,
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- ============================================
-- DATOS DE PRUEBA
-- ============================================

-- Insertar usuarios de prueba
INSERT INTO usuarios (username, password, rol) VALUES
('admin', 'admin123', 'SUPERVISOR'),
('cajero1', 'cajero123', 'CAJERO'),
('cajero2', 'cajero456', 'CAJERO')
ON CONFLICT (username) DO NOTHING;

-- Insertar proveedores de prueba
INSERT INTO proveedores (ruc, nombre, telefono, direccion, razon_social) VALUES
('20123456789', 'Distribuidora ABC S.A.', '987654321', 'Av. Principal 123', 'Distribuidora ABC Sociedad Anónima'),
('20234567890', 'Importadora XYZ', '987654322', 'Jr. Comercio 456', 'Importadora XYZ E.I.R.L.'),
('20345678901', 'Mayorista 123', '987654323', 'Calle Mayor 789', 'Mayorista 123 S.A.C.')
ON CONFLICT (ruc) DO NOTHING;

-- Insertar clientes de prueba
INSERT INTO clientes (dni, nombre, direccion, telefono) VALUES
('12345678', 'Juan Pérez García', 'Av. Los Olivos 123', '987654321'),
('23456789', 'María López Torres', 'Jr. San Martín 456', '987654322'),
('34567890', 'Carlos Rodríguez Silva', 'Calle Real 789', '987654323'),
('45678901', 'Ana Martínez Díaz', 'Av. Libertad 321', '987654324')
ON CONFLICT (dni) DO NOTHING;

-- Insertar productos de prueba
INSERT INTO productos (codigo, descripcion, stock, precio_unit, precio_mayor, proveedor_id) VALUES
('PROD001', 'Laptop HP 15.6"', 10, 2500.00, 2300.00, 1),
('PROD002', 'Mouse Logitech M100', 50, 25.00, 20.00, 1),
('PROD003', 'Teclado Mecánico RGB', 30, 150.00, 130.00, 2),
('PROD004', 'Monitor LG 24" Full HD', 15, 450.00, 420.00, 2),
('PROD005', 'Auriculares Sony WH-1000XM4', 20, 800.00, 750.00, 3),
('PROD006', 'Webcam Logitech C920', 25, 180.00, 160.00, 1),
('PROD007', 'Disco Duro Externo 1TB', 40, 120.00, 100.00, 3),
('PROD008', 'Memoria USB 32GB', 100, 15.00, 12.00, 2)
ON CONFLICT (codigo) DO NOTHING;

-- ============================================
-- VISTAS ÚTILES
-- ============================================

-- Vista: Resumen de ventas por cliente
CREATE OR REPLACE VIEW vw_ventas_por_cliente AS
SELECT 
    c.id AS cliente_id,
    c.dni,
    c.nombre AS cliente_nombre,
    COUNT(v.id) AS total_ventas,
    COALESCE(SUM(v.total), 0) AS monto_total
FROM clientes c
LEFT JOIN ventas v ON c.id = v.cliente_id
GROUP BY c.id, c.dni, c.nombre;

-- Vista: Productos con bajo stock (menos de 10 unidades)
CREATE OR REPLACE VIEW vw_productos_bajo_stock AS
SELECT 
    p.id,
    p.codigo,
    p.descripcion,
    p.stock,
    p.precio_unit,
    pr.nombre AS proveedor_nombre
FROM productos p
INNER JOIN proveedores pr ON p.proveedor_id = pr.id
WHERE p.stock < 10
ORDER BY p.stock ASC;

-- Vista: Ventas del día
CREATE OR REPLACE VIEW vw_ventas_hoy AS
SELECT 
    v.id,
    v.fecha,
    c.nombre AS cliente_nombre,
    v.tipo_doc,
    v.tipo_venta,
    v.total
FROM ventas v
INNER JOIN clientes c ON v.cliente_id = c.id
WHERE DATE(v.fecha) = CURRENT_DATE
ORDER BY v.fecha DESC;

-- ============================================
-- FUNCIONES Y PROCEDIMIENTOS ALMACENADOS
-- ============================================

-- Función: Obtener estadísticas de ventas
CREATE OR REPLACE FUNCTION sp_estadisticas_ventas(
    fecha_inicio DATE,
    fecha_fin DATE
)
RETURNS TABLE (
    total_ventas BIGINT,
    monto_total NUMERIC,
    promedio_venta NUMERIC,
    venta_minima NUMERIC,
    venta_maxima NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        COUNT(*)::BIGINT AS total_ventas,
        COALESCE(SUM(v.total), 0) AS monto_total,
        COALESCE(AVG(v.total), 0) AS promedio_venta,
        COALESCE(MIN(v.total), 0) AS venta_minima,
        COALESCE(MAX(v.total), 0) AS venta_maxima
    FROM ventas v
    WHERE DATE(v.fecha) BETWEEN fecha_inicio AND fecha_fin;
END;
$$ LANGUAGE plpgsql;

-- Función: Actualizar stock después de venta
CREATE OR REPLACE FUNCTION sp_actualizar_stock_venta(
    producto_id_param INT,
    cantidad_vendida INT
)
RETURNS INT AS $$
DECLARE
    filas_afectadas INT;
BEGIN
    UPDATE productos 
    SET stock = stock - cantidad_vendida
    WHERE id = producto_id_param AND stock >= cantidad_vendida;
    
    GET DIAGNOSTICS filas_afectadas = ROW_COUNT;
    RETURN filas_afectadas;
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- TRIGGERS
-- ============================================

-- Función para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION actualizar_fecha_actualizacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Actualizar fecha_actualizacion en productos
CREATE TRIGGER tr_actualizar_fecha_producto
BEFORE UPDATE ON productos
FOR EACH ROW
EXECUTE FUNCTION actualizar_fecha_actualizacion();

-- Función para validar total de venta
CREATE OR REPLACE FUNCTION validar_total_venta()
RETURNS TRIGGER AS $$
BEGIN
    IF ABS(NEW.total - (NEW.subtotal + NEW.igv)) > 0.01 THEN
        RAISE EXCEPTION 'El total debe ser igual a subtotal + IGV';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Validar que el total sea igual a subtotal + IGV
CREATE TRIGGER tr_validar_total_venta
BEFORE INSERT OR UPDATE ON ventas
FOR EACH ROW
EXECUTE FUNCTION validar_total_venta();

-- Función para log de actualización de productos
CREATE OR REPLACE FUNCTION log_actualizacion_producto()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.stock != NEW.stock THEN
        INSERT INTO productos_log (producto_id, stock_anterior, stock_nuevo, fecha_cambio)
        VALUES (NEW.id, OLD.stock, NEW.stock, CURRENT_TIMESTAMP);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Log de actualización de productos
CREATE TRIGGER tr_log_actualizacion_producto
AFTER UPDATE ON productos
FOR EACH ROW
EXECUTE FUNCTION log_actualizacion_producto();

-- ============================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- ============================================
CREATE INDEX IF NOT EXISTS idx_ventas_fecha_cliente ON ventas(fecha, cliente_id);
CREATE INDEX IF NOT EXISTS idx_venta_detalle_venta_producto ON venta_detalle(venta_id, producto_id);

-- ============================================
-- FIN DEL SCRIPT
-- ============================================


