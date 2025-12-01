package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final ProductoService productoService;

    public CarritoController(ProductoService productoService) {
        this.productoService = productoService;
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

    @GetMapping("/add/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        Map<Long, CarritoItem> carrito = getCarrito(session);

        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

        carrito.compute(id, (key, item) ->
                item == null ? new CarritoItem(producto, 1) : new CarritoItem(producto, item.getCantidad() + 1)
        );

        // Volver a la tienda
        return "redirect:/tienda";
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Map<Long, CarritoItem> carrito = getCarrito(session);

        BigDecimal total = carrito.values().stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("items", carrito.values());
        model.addAttribute("total", total);

        return "carrito";
    }

    @PostMapping("/update/{id}")
    public String actualizarCantidad(@PathVariable Long id,
                                     @RequestParam("cantidad") int cantidad,
                                     HttpSession session) {

        Map<Long, CarritoItem> carrito = getCarrito(session);

        if (carrito.containsKey(id)) {
            if (cantidad > 0) {
                carrito.get(id).setCantidad(cantidad);
            } else {
                carrito.remove(id);
            }
        }

        return "redirect:/carrito";
    }

    @GetMapping("/remove/{id}")
    public String quitarProducto(@PathVariable Long id, HttpSession session) {
        getCarrito(session).remove(id);
        return "redirect:/carrito";
    }
}
