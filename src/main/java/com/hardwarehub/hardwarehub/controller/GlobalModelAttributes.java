package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UsuarioService usuarioService;

    public GlobalModelAttributes(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        Map<Long, CarritoItem> carrito = (Map<Long, CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            return 0;
        }
        return carrito.values().stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();
    }

    @ModelAttribute("currentUser")
    public Usuario currentUser() {
        return usuarioService.findActual().orElse(null);
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        return usuarioService.findActual().isPresent();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Optional<Usuario> usuario = usuarioService.findActual();
        return usuario.isPresent() && Rol.ADMIN.equals(usuario.get().getRol());
    }

    @ModelAttribute("isVendedor")
    public boolean isVendedor() {
        Optional<Usuario> usuario = usuarioService.findActual();
        return usuario.isPresent() && Rol.VENDEDOR.equals(usuario.get().getRol());
    }

    @ModelAttribute("isCliente")
    public boolean isCliente() {
        Optional<Usuario> usuario = usuarioService.findActual();
        return usuario.isPresent() && Rol.CLIENTE.equals(usuario.get().getRol());
    }
}
