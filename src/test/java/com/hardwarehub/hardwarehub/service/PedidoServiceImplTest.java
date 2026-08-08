package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import com.hardwarehub.hardwarehub.model.Pedido;
import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.repository.LineaPedidoRepository;
import com.hardwarehub.hardwarehub.repository.PedidoRepository;
import com.hardwarehub.hardwarehub.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private LineaPedidoRepository lineaPedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void checkoutBloqueaElProductoAntesDeDescontarStock() {
        Usuario vendedor = usuario(20L, Rol.VENDEDOR);
        Usuario comprador = usuario(10L, Rol.CLIENTE);
        Producto producto = producto(1L, "GPU", "125.50", 1, vendedor);
        Map<Long, CarritoItem> carrito = Map.of(1L, new CarritoItem(producto, 1));

        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Pedido pedido = pedidoService.finalizarCompra(carrito, comprador);

        assertThat(producto.getStock()).isZero();
        assertThat(pedido.getTotal()).isEqualByComparingTo("125.50");
        assertThat(pedido.getLineas()).hasSize(1);
        assertThat(pedido.getLineas().get(0).getVendedor()).isSameAs(vendedor);
        verify(productoRepository).findByIdForUpdate(1L);
        verify(productoRepository).save(producto);
    }

    @Test
    void checkoutNoModificaStockSiLaCantidadNoEstaDisponible() {
        Usuario vendedor = usuario(20L, Rol.VENDEDOR);
        Producto producto = producto(1L, "GPU", "125.50", 1, vendedor);
        Map<Long, CarritoItem> carrito = Map.of(1L, new CarritoItem(producto, 2));

        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> pedidoService.finalizarCompra(carrito, usuario(10L, Rol.CLIENTE)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock suficiente");

        assertThat(producto.getStock()).isEqualTo(1);
        verify(productoRepository, never()).save(any(Producto.class));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void checkoutAdquiereBloqueosEnOrdenDeProducto() {
        Usuario vendedor = usuario(20L, Rol.VENDEDOR);
        Producto productoDos = producto(2L, "GPU", "200.00", 5, vendedor);
        Producto productoUno = producto(1L, "CPU", "100.00", 5, vendedor);
        Map<Long, CarritoItem> carrito = new LinkedHashMap<>();
        carrito.put(2L, new CarritoItem(productoDos, 1));
        carrito.put(1L, new CarritoItem(productoUno, 1));

        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(productoUno));
        when(productoRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(productoDos));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        pedidoService.finalizarCompra(carrito, usuario(10L, Rol.CLIENTE));

        InOrder orden = inOrder(productoRepository);
        orden.verify(productoRepository).findByIdForUpdate(1L);
        orden.verify(productoRepository).findByIdForUpdate(2L);
    }

    private static Usuario usuario(Long id, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        return usuario;
    }

    private static Producto producto(Long id, String nombre, String precio, int stock, Usuario vendedor) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecio(new BigDecimal(precio));
        producto.setStock(stock);
        producto.setVendedor(vendedor);
        return producto;
    }
}
