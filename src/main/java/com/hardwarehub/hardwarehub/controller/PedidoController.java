package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.LineaPedido;
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

import java.math.BigDecimal;
import java.util.List;

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
        List<LineaPedido> lineasVendedor = pedido.getLineas().stream()
                .filter(linea -> linea.getVendedor().getId().equals(usuario.getId()))
                .toList();

        if (!esComprador && !esAdmin && lineasVendedor.isEmpty()) {
            return "redirect:/tienda";
        }

        // Correccion posterior asistida por IA: un vendedor solo recibe sus propias lineas.
        boolean vistaVendedor = !esComprador && !esAdmin;
        List<LineaPedido> lineasVisibles = vistaVendedor ? lineasVendedor : pedido.getLineas();
        BigDecimal totalVisible = vistaVendedor
                ? lineasVendedor.stream()
                        .map(LineaPedido::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                : pedido.getTotal();

        model.addAttribute("pedido", pedido);
        model.addAttribute("lineasVisibles", lineasVisibles);
        model.addAttribute("totalVisible", totalVisible);
        model.addAttribute("vistaVendedor", vistaVendedor);
        return "pedido-detalle";
    }
}
