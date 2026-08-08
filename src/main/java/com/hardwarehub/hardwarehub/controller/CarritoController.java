package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import com.hardwarehub.hardwarehub.model.Pedido;
import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.PedidoService;
import com.hardwarehub.hardwarehub.service.ProductoService;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public CarritoController(ProductoService productoService,
                             PedidoService pedidoService,
                             UsuarioService usuarioService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CarritoItem> getCarrito(HttpSession session) {
        Map<Long, CarritoItem> carrito = (Map<Long, CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    @PostMapping("/add/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Long, CarritoItem> carrito = getCarrito(session);

        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

        if (producto.getStock() == null || producto.getStock() <= 0) {
            redirectAttributes.addFlashAttribute("mensaje", "El producto está agotado.");
            return "redirect:/tienda";
        }

        CarritoItem itemActual = carrito.get(id);
        int cantidadActual = itemActual == null ? 0 : itemActual.getCantidad();

        if (cantidadActual + 1 > producto.getStock()) {
            redirectAttributes.addFlashAttribute("mensaje", "No puedes añadir más unidades que el stock disponible.");
            return "redirect:/tienda";
        }

        carrito.put(id, new CarritoItem(producto, cantidadActual + 1));

        return "redirect:/tienda";
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model, @ModelAttribute("mensaje") String mensaje) {
        Map<Long, CarritoItem> carrito = getCarrito(session);

        BigDecimal total = carrito.values().stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("items", carrito.values());
        model.addAttribute("total", total);
        model.addAttribute("mensaje", mensaje);

        return "carrito";
    }

    @PostMapping("/update/{id}")
    public String actualizarCantidad(@PathVariable Long id,
                                     @RequestParam("cantidad") int cantidad,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        Map<Long, CarritoItem> carrito = getCarrito(session);

        if (carrito.containsKey(id)) {
            if (cantidad > 0) {
                Producto producto = productoService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

                if (cantidad > producto.getStock()) {
                    cantidad = producto.getStock();
                    redirectAttributes.addFlashAttribute("mensaje", "Cantidad ajustada al stock disponible.");
                }

                if (cantidad > 0) {
                    carrito.get(id).setCantidad(cantidad);
                } else {
                    carrito.remove(id);
                }
            } else {
                carrito.remove(id);
            }
        }

        return "redirect:/carrito";
    }

    @PostMapping("/remove/{id}")
    public String quitarProducto(@PathVariable Long id, HttpSession session) {
        getCarrito(session).remove(id);
        return "redirect:/carrito";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario comprador = usuarioService.findActual()
                .orElseThrow(() -> new IllegalArgumentException("Debes iniciar sesión."));

        Map<Long, CarritoItem> carrito = getCarrito(session);

        try {
            Pedido pedido = pedidoService.finalizarCompra(carrito, comprador);
            carrito.clear();
            redirectAttributes.addFlashAttribute("mensaje", "Compra realizada correctamente.");
            return "redirect:/pedidos/" + pedido.getId();
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/carrito";
        }
    }
}
