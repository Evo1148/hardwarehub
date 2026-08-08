-- data.sql idempotente para la versión marketplace
-- Se ejecuta al arrancar la aplicación si spring.sql.init.mode=always

-- USUARIOS DE PRUEBA
-- Contraseñas:
-- admin@hardwarehub.com / admin123
-- vendedor@hardwarehub.com / vendedor123
-- cliente@hardwarehub.com / cliente123
-- Uso {noop} para que los usuarios iniciales sean fáciles de probar. Los usuarios registrados desde la web se guardan cifrados.

INSERT INTO usuarios (nombre, email, password, rol, activo)
SELECT * FROM (SELECT 'Administrador' AS nombre, 'admin@hardwarehub.com' AS email, '{noop}admin123' AS password, 'ADMIN' AS rol, true AS activo) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@hardwarehub.com');

INSERT INTO usuarios (nombre, email, password, rol, activo)
SELECT * FROM (SELECT 'Vendedor Demo' AS nombre, 'vendedor@hardwarehub.com' AS email, '{noop}vendedor123' AS password, 'VENDEDOR' AS rol, true AS activo) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'vendedor@hardwarehub.com');

INSERT INTO usuarios (nombre, email, password, rol, activo)
SELECT * FROM (SELECT 'Cliente Demo' AS nombre, 'cliente@hardwarehub.com' AS email, '{noop}cliente123' AS password, 'CLIENTE' AS rol, true AS activo) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'cliente@hardwarehub.com');

-- CATEGORÍAS
INSERT INTO categorias (nombre, descripcion)
SELECT * FROM (SELECT 'Tarjetas Gráficas' AS nombre, 'GPUs de alta gama para gaming y trabajo profesional' AS descripcion) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Tarjetas Gráficas');

INSERT INTO categorias (nombre, descripcion)
SELECT * FROM (SELECT 'Procesadores' AS nombre, 'CPUs AMD e Intel de última generación' AS descripcion) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Procesadores');

INSERT INTO categorias (nombre, descripcion)
SELECT * FROM (SELECT 'Placas Base' AS nombre, 'Motherboards compatibles con varios sockets' AS descripcion) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Placas Base');

INSERT INTO categorias (nombre, descripcion)
SELECT * FROM (SELECT 'Periféricos' AS nombre, 'Teclados, ratones, auriculares y más' AS descripcion) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Periféricos');

INSERT INTO categorias (nombre, descripcion)
SELECT * FROM (SELECT 'Almacenamiento' AS nombre, 'Discos SSD, NVMe y almacenamiento externo' AS descripcion) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Almacenamiento');

-- PRODUCTOS DEL VENDEDOR DEMO
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, vendedor_id, imagen)
SELECT * FROM (
  SELECT
    'NVIDIA RTX 4070' AS nombre,
    'Tarjeta gráfica de última generación con excelente rendimiento en 1440p' AS descripcion,
    699.99 AS precio,
    12 AS stock,
    (SELECT id FROM categorias WHERE nombre = 'Tarjetas Gráficas' LIMIT 1) AS categoria_id,
    (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1) AS vendedor_id,
    'https://thumb.pccomponentes.com/w-530-530/articles/1070/10706586/1500-gigabyte-geforce-rtx-4070-aero-oc-12gb-gddr6x-dlss3.jpg' AS imagen
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'NVIDIA RTX 4070');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, vendedor_id, imagen)
SELECT * FROM (
  SELECT
    'AMD Ryzen 7 5800X' AS nombre,
    'Procesador de 8 núcleos y 16 hilos ideal para gaming y productividad' AS descripcion,
    299.99 AS precio,
    18 AS stock,
    (SELECT id FROM categorias WHERE nombre = 'Procesadores' LIMIT 1) AS categoria_id,
    (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1) AS vendedor_id,
    'https://m.media-amazon.com/images/I/61DYLoyNRWL._AC_SL1500_.jpg' AS imagen
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'AMD Ryzen 7 5800X');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, vendedor_id, imagen)
SELECT * FROM (
  SELECT
    'Intel Core i7-12700K' AS nombre,
    'CPU híbrida con núcleos de rendimiento y eficiencia' AS descripcion,
    379.99 AS precio,
    10 AS stock,
    (SELECT id FROM categorias WHERE nombre = 'Procesadores' LIMIT 1) AS categoria_id,
    (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1) AS vendedor_id,
    'https://m.media-amazon.com/images/I/71iNHGS2w5L._AC_SL1500_.jpg' AS imagen
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Intel Core i7-12700K');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, vendedor_id, imagen)
SELECT * FROM (
  SELECT
    'ASUS ROG Strix B550-F' AS nombre,
    'Placa base de alto rendimiento AM4 para gaming' AS descripcion,
    189.99 AS precio,
    8 AS stock,
    (SELECT id FROM categorias WHERE nombre = 'Placas Base' LIMIT 1) AS categoria_id,
    (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1) AS vendedor_id,
    'https://thumb.pccomponentes.com/w-530-530/articles/32/325655/1580-asus-rog-strix-b550-f-gaming-wifi-ii.jpg' AS imagen
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'ASUS ROG Strix B550-F');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, vendedor_id, imagen)
SELECT * FROM (
  SELECT
    'Teclado Mecánico RGB' AS nombre,
    'Teclado mecánico con switches rojos y retroiluminación RGB' AS descripcion,
    89.99 AS precio,
    25 AS stock,
    (SELECT id FROM categorias WHERE nombre = 'Periféricos' LIMIT 1) AS categoria_id,
    (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1) AS vendedor_id,
    'https://m.media-amazon.com/images/I/71R+6aF8cOL._AC_SL1500_.jpg' AS imagen
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Teclado Mecánico RGB');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, vendedor_id, imagen)
SELECT * FROM (
  SELECT
    'SSD NVMe 1TB' AS nombre,
    'Almacenamiento ultrarrápido PCIe 3.0 NVMe' AS descripcion,
    99.99 AS precio,
    30 AS stock,
    (SELECT id FROM categorias WHERE nombre = 'Almacenamiento' LIMIT 1) AS categoria_id,
    (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1) AS vendedor_id,
    'https://m.media-amazon.com/images/I/71vpl6RD4vL._AC_SL1500_.jpg' AS imagen
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'SSD NVMe 1TB');

-- Si ya existían productos de la versión anterior sin vendedor, se asignan al vendedor demo.
UPDATE productos
SET vendedor_id = (SELECT id FROM usuarios WHERE email = 'vendedor@hardwarehub.com' LIMIT 1)
WHERE vendedor_id IS NULL;
