-- init.sql: se ejecuta sólo la primera vez que se inicializa el volumen de MySQL
-- Esquema actualizado para HardwareHub Marketplace

CREATE DATABASE IF NOT EXISTS `hardwarehub_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hardwarehub_db`;

CREATE TABLE IF NOT EXISTS usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  rol VARCHAR(30) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS categorias (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS productos (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL UNIQUE,
  descripcion VARCHAR(1000) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  categoria_id BIGINT NOT NULL,
  vendedor_id BIGINT,
  imagen VARCHAR(1024),
  CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_producto_vendedor FOREIGN KEY (vendedor_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pedidos (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  comprador_id BIGINT NOT NULL,
  fecha DATETIME NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_pedido_comprador FOREIGN KEY (comprador_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS lineas_pedido (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  pedido_id BIGINT NOT NULL,
  producto_id BIGINT NOT NULL,
  vendedor_id BIGINT NOT NULL,
  nombre_producto VARCHAR(255) NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  cantidad INT NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_linea_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_linea_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_linea_vendedor FOREIGN KEY (vendedor_id) REFERENCES usuarios(id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO usuarios (nombre, email, password, rol, activo)
SELECT * FROM (SELECT 'Administrador', 'admin@hardwarehub.com', '{noop}admin123', 'ADMIN', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@hardwarehub.com');

INSERT INTO usuarios (nombre, email, password, rol, activo)
SELECT * FROM (SELECT 'Vendedor Demo', 'vendedor@hardwarehub.com', '{noop}vendedor123', 'VENDEDOR', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'vendedor@hardwarehub.com');

INSERT INTO usuarios (nombre, email, password, rol, activo)
SELECT * FROM (SELECT 'Cliente Demo', 'cliente@hardwarehub.com', '{noop}cliente123', 'CLIENTE', true) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'cliente@hardwarehub.com');
