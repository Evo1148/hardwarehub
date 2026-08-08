package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Pedido;
import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.PedidoService;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String misPedidos(Model model) {
        Usuario usuario = usuarioService.findActual()
                .orElseThrow(() -> new IllegalArgumentException("Debes iniciar sesión."));

        model.addAttribute("pedidos", pedidoService.findPedidosComprador(usuario.getId()));
        return "pedidos";
    }

    @GetMapping("/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findActual()
                .orElseThrow(() -> new IllegalArgumentException("Debes iniciar sesión."));

        Pedido pedido = pedidoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado."));

        boolean esComprador = pedido.getComprador().getId().equals(usuario.getId());
        boolean esAdmin = Rol.ADMIN.equals(usuario.getRol());
        boolean esVendedorDelPedido = pedido.getLineas().stream()
                .anyMatch(linea -> linea.getVendedor().getId().equals(usuario.getId()));

        if (!esComprador && !esAdmin && !esVendedorDelPedido) {
            return "redirect:/tienda";
        }

        model.addAttribute("pedido", pedido);
        return "pedido-detalle";
    }
}
