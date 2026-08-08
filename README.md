# HardwareHub Marketplace

Proyecto web hecho con Spring Boot, Thymeleaf, Spring Data JPA, Spring Security y MySQL.

La aplicación empezó como una tienda sencilla de componentes de ordenador y en esta versión la he cambiado para que funcione más como un marketplace. La idea es que no exista solo un administrador metiendo productos, sino que también haya usuarios vendedores que puedan publicar sus propios productos y usuarios clientes que puedan comprarlos.

## Qué se puede hacer

- Ver el catálogo público desde `/tienda`.
- Buscar productos por nombre y filtrar por categoría.
- Registrarse como cliente o como vendedor.
- Iniciar sesión con email y contraseña.
- Añadir productos al carrito.
- Finalizar una compra de prueba, creando un pedido en la base de datos.
- Descontar stock cuando se confirma la compra.
- Consultar las compras realizadas desde `/pedidos`.
- Si eres vendedor, publicar productos y ver las ventas de tus productos.
- Si eres administrador, gestionar categorías y ver todos los productos.

## Roles usados

Hay tres tipos de usuario:

| Rol | Uso |
|---|---|
| ADMIN | Gestiona categorías y puede ver todo el contenido de la aplicación. |
| VENDEDOR | Publica productos, modifica sus productos y consulta sus ventas. |
| CLIENTE | Compra productos y consulta sus pedidos. |

Desde el formulario de registro público solo se permite crear cuentas de cliente o vendedor. El administrador se deja creado en los datos iniciales para poder probar la aplicación.

## Usuarios de prueba

Al arrancar el proyecto se insertan estos usuarios si no existen:

| Rol | Email | Contraseña |
|---|---|---|
| Admin | `admin@hardwarehub.com` | `admin123` |
| Vendedor | `vendedor@hardwarehub.com` | `vendedor123` |
| Cliente | `cliente@hardwarehub.com` | `cliente123` |

Para estos usuarios iniciales uso `{noop}` en la contraseña para poder probar rápido. Los usuarios creados desde la pantalla de registro sí se guardan usando el codificador configurado en Spring Security.

## Base de datos con Docker

Desde la carpeta del proyecto:

```bash
docker compose up -d
```

Servicios que levanta:

- MySQL en `localhost:3306`
- Adminer en `http://localhost:8081`

Datos para entrar en Adminer:

```text
Servidor: mysql
Usuario: hwuser
Contraseña: hwpass
Base de datos: hardwarehub_db
```

## Ejecutar la aplicación

Con Maven instalado:

```bash
mvn spring-boot:run
```

Después se entra desde:

```text
http://localhost:8080
```

## Rutas principales

| Ruta | Descripción |
|---|---|
| `/tienda` | Catálogo público del marketplace. |
| `/login` | Inicio de sesión. |
| `/registro` | Registro de cliente o vendedor. |
| `/carrito` | Carrito guardado en sesión. |
| `/pedidos` | Pedidos del usuario conectado. |
| `/productos` | Gestión de productos del vendedor o de todos si es admin. |
| `/vendedor/ventas` | Ventas de los productos del vendedor. |
| `/categorias` | Gestión de categorías, solo para admin. |

## Algunas decisiones del proyecto

El carrito lo he dejado en sesión porque para esta práctica era suficiente y simplifica bastante la aplicación. Cuando se finaliza la compra, entonces sí se crea un `Pedido` con sus `LineaPedido` en la base de datos.

También he cambiado las acciones que modifican datos para que usen formularios `POST` en vez de enlaces `GET`. Por ejemplo, añadir al carrito o eliminar productos/categorías ya no se hace con un simple enlace. Además, he dejado CSRF activado, por lo que los formularios tienen el token correspondiente.

Una cosa importante es que cada producto tiene un vendedor asociado. Al crear un pedido, cada línea guarda el producto, el vendedor, el precio usado en ese momento y la cantidad. Así, aunque más adelante cambie el precio del producto, queda constancia del precio al que se compró.

## Cosas que se podrían mejorar

- Añadir subida real de imágenes en vez de usar una URL.
- Separar mejor la parte de administración de la parte de vendedor.
- Añadir estados al pedido: pendiente, pagado, enviado, cancelado, etc.
- Mejorar el control de errores en algunas pantallas.
- Añadir tests.
- Hacer un sistema de pago real o simulado con más detalle.
