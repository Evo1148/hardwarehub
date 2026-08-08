# Cambios hechos para la versión marketplace

Este fichero lo dejo como resumen de los cambios principales respecto a la versión inicial de HardwareHub.

## Usuarios

He añadido una entidad `Usuario` y un enum `Rol`. La aplicación trabaja con tres roles:

- `ADMIN`
- `VENDEDOR`
- `CLIENTE`

Los ficheros más importantes de esta parte son:

- `model/Usuario.java`
- `model/Rol.java`
- `repository/UsuarioRepository.java`
- `service/UsuarioService.java`
- `security/UsuarioDetailsService.java`
- `config/SecurityConfig.java`

## Login y registro

Se ha añadido Spring Security para poder iniciar sesión con email y contraseña. También hay una pantalla de registro donde el usuario puede elegir si quiere ser cliente o vendedor.

No se permite crear administradores desde el registro público. El administrador se crea desde los datos iniciales.

## Productos con vendedor

Antes los productos estaban en la tienda sin distinguir quién los había publicado. Ahora `Producto` tiene una relación con `Usuario vendedor`.

Esto permite que:

- El vendedor vea solo sus productos en `/productos`.
- El administrador vea todos los productos.
- En la tienda se muestre el nombre del vendedor.
- En las ventas se pueda saber quién vendió cada producto.

## Carrito y pedidos

El carrito sigue siendo un atributo de sesión. Al confirmar la compra se crea un pedido real en la base de datos.

Se han añadido estas clases:

- `Pedido`
- `LineaPedido`
- `PedidoRepository`
- `LineaPedidoRepository`
- `PedidoService`
- `PedidoController`

El proceso de compra hace lo siguiente:

1. Comprueba que el carrito no esté vacío.
2. Vuelve a buscar los productos en base de datos.
3. Comprueba que haya stock suficiente.
4. Crea el pedido y sus líneas.
5. Descuenta el stock.
6. Vacía el carrito.
7. Redirige al detalle del pedido.

## Ventas del vendedor

He añadido la ruta `/vendedor/ventas`. Sirve para que un vendedor vea quién ha comprado sus productos.

En esa pantalla se muestra:

- Fecha.
- Producto vendido.
- Comprador y email.
- Cantidad.
- Subtotal.
- Enlace al pedido.

El administrador puede entrar en la misma pantalla y ver todas las ventas.

## Cambios de seguridad y rutas

He cambiado las acciones que modifican información para que sean `POST`. Antes algunas acciones estaban como enlaces `GET`, que era más cómodo pero peor planteado.

Cambios destacados:

- Añadir al carrito ahora usa `POST`.
- Quitar del carrito ahora usa `POST`.
- Eliminar productos ahora usa `POST`.
- Eliminar categorías ahora usa `POST`.
- CSRF queda activado y los formularios tienen el token.

## Plantillas modificadas o añadidas

Plantillas nuevas:

- `login.html`
- `registro.html`
- `pedidos.html`
- `pedido-detalle.html`
- `ventas-vendedor.html`

Plantillas modificadas:

- `layout.html`
- `tienda.html`
- `producto-detalle.html`
- `producto-form.html`
- `productos.html`
- `carrito.html`
- `categorias.html`

## Datos iniciales

En `data.sql` se crean usuarios, categorías y algunos productos de prueba si todavía no existen. También se asignan al vendedor demo los productos antiguos que no tuvieran vendedor.
