package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.PedidoService;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public VendedorController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/ventas")
    public String misVentas(Model model) {
        Usuario usuario = usuarioService.findActual()
                .orElseThrow(() -> new IllegalArgumentException("Debes iniciar sesión."));

        if (Rol.ADMIN.equals(usuario.getRol())) {
            model.addAttribute("ventas", pedidoService.findTodasLasVentas());
            model.addAttribute("vistaAdmin", true);
        } else {
            model.addAttribute("ventas", pedidoService.findVentasVendedor(usuario.getId()));
            model.addAttribute("vistaAdmin", false);
        }

        return "ventas-vendedor";
    }
}
