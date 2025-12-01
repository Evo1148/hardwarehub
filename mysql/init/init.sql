-- init.sql: se ejecuta sólo la primera vez que se inicializa el volumen de MySQL
CREATE DATABASE IF NOT EXISTS `hardwarehub_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hardwarehub_db`;

-- Tabla categorias
CREATE TABLE IF NOT EXISTS categorias (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  descripcion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla productos
CREATE TABLE IF NOT EXISTS productos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  descripcion TEXT,
  precio DECIMAL(10,2) DEFAULT 0.00,
  stock INT DEFAULT 0,
  categoria_id BIGINT,
  imagen VARCHAR(1024),
  FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos iniciales (idempotentes usando INSERT IGNORE)
INSERT IGNORE INTO categorias (nombre, descripcion) VALUES
('Tarjetas Gráficas', 'GPUs de alta gama para gaming y trabajo profesional'),
('Procesadores', 'CPUs AMD e Intel de última generación'),
('Placas Base', 'Motherboards compatibles con varios sockets'),
('Periféricos', 'Teclados, ratones, auriculares y más'),
('Almacenamiento', 'Discos SSD, NVMe y almacenamiento externo');

INSERT IGNORE INTO productos (nombre, descripcion, precio, stock, categoria_id, imagen) VALUES
('NVIDIA RTX 4070', 'Tarjeta gráfica de última generación con excelente rendimiento en 1440p', 699.99, 12, 1, 'https://thumb.pccomponentes.com/w-530-530/articles/1070/10706586/1500-gigabyte-geforce-rtx-4070-aero-oc-12gb-gddr6x-dlss3.jpg'),
('AMD Ryzen 7 5800X', 'Procesador de 8 núcleos y 16 hilos ideal para gaming y productividad', 299.99, 18, 2, 'https://m.media-amazon.com/images/I/61DYLoyNRWL._AC_SL1500_.jpg'),
('Intel Core i7-12700K', 'CPU híbrida con núcleos de rendimiento y eficiencia', 379.99, 10, 2, 'https://m.media-amazon.com/images/I/71iNHGS2w5L._AC_SL1500_.jpg'),
('ASUS ROG Strix B550-F', 'Placa base de alto rendimiento AM4 para gaming', 189.99, 8, 3, 'https://thumb.pccomponentes.com/w-530-530/articles/32/325655/1580-asus-rog-strix-b550-f-gaming-wifi-ii.jpg'),
('Teclado Mecánico RGB', 'Teclado mecánico con switches rojos y retroiluminación RGB', 89.99, 25, 4, 'https://m.media-amazon.com/images/I/71R+6aF8cOL._AC_SL1500_.jpg'),
('SSD NVMe 1TB', 'Almacenamiento ultrarrápido PCIe 3.0 NVMe', 99.99, 30, 5, 'https://m.media-amazon.com/images/I/71vpl6RD4vL._AC_SL1500_.jpg');

