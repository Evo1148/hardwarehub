package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalModelAttributes {

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
}
