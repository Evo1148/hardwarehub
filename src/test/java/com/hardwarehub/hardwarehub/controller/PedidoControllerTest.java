package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.LineaPedido;
import com.hardwarehub.hardwarehub.model.Pedido;
import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.PedidoService;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PedidoController controller;

    @Test
    void vendedorSoloVeSusLineasYSuTotal() {
        Usuario comprador = usuario(10L, "Comprador", Rol.CLIENTE);
        Usuario vendedorActual = usuario(20L, "Vendedor actual", Rol.VENDEDOR);
        Usuario otroVendedor = usuario(30L, "Otro vendedor", Rol.VENDEDOR);

        LineaPedido lineaPropia = linea(vendedorActual, "Producto propio", "50.00");
        LineaPedido lineaAjena = linea(otroVendedor, "Producto ajeno", "80.00");
        Pedido pedido = pedido(99L, comprador, lineaPropia, lineaAjena);

        when(usuarioService.findActual()).thenReturn(Optional.of(vendedorActual));
        when(pedidoService.findById(99L)).thenReturn(Optional.of(pedido));

        ConcurrentModel model = new ConcurrentModel();
        String vista = controller.detallePedido(99L, model);

        assertThat(vista).isEqualTo("pedido-detalle");
        assertThat(model.getAttribute("lineasVisibles")).isEqualTo(List.of(lineaPropia));
        assertThat(model.getAttribute("totalVisible")).isEqualTo(new BigDecimal("50.00"));
        assertThat(model.getAttribute("vistaVendedor")).isEqualTo(true);
    }

    @Test
    void usuarioAjenoAlPedidoNoPuedeVerElDetalle() {
        Usuario comprador = usuario(10L, "Comprador", Rol.CLIENTE);
        Usuario vendedor = usuario(20L, "Vendedor", Rol.VENDEDOR);
        Usuario usuarioAjeno = usuario(40L, "Usuario ajeno", Rol.VENDEDOR);
        Pedido pedido = pedido(99L, comprador, linea(vendedor, "Producto", "50.00"));

        when(usuarioService.findActual()).thenReturn(Optional.of(usuarioAjeno));
        when(pedidoService.findById(99L)).thenReturn(Optional.of(pedido));

        String vista = controller.detallePedido(99L, new ConcurrentModel());

        assertThat(vista).isEqualTo("redirect:/tienda");
    }

    private static Usuario usuario(Long id, String nombre, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setRol(rol);
        return usuario;
    }

    private static LineaPedido linea(Usuario vendedor, String nombre, String subtotal) {
        LineaPedido linea = new LineaPedido();
        linea.setVendedor(vendedor);
        linea.setNombreProducto(nombre);
        linea.setSubtotal(new BigDecimal(subtotal));
        return linea;
    }

    private static Pedido pedido(Long id, Usuario comprador, LineaPedido... lineas) {
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setComprador(comprador);
        pedido.setLineas(List.of(lineas));
        pedido.setTotal(List.of(lineas).stream()
                .map(LineaPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return pedido;
    }
}
