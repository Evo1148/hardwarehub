package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.service.CategoriaService;
import com.hardwarehub.hardwarehub.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public TiendaController(ProductoService productoService,
                            CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String catalogo(@RequestParam(name = "q", required = false) String q,
                           @RequestParam(name = "categoriaId", required = false) Long categoriaId,
                           Model model) {

        model.addAttribute("productos", productoService.buscar(q, categoriaId));
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("q", q);
        model.addAttribute("categoriaId", categoriaId);

        return "tienda";
    }

    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));

        model.addAttribute("producto", producto);
        return "producto-detalle";
    }
}
