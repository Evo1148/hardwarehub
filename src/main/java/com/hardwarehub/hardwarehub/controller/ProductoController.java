package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.service.CategoriaService;
import com.hardwarehub.hardwarehub.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService,
                              CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos";
    }

    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        return "producto-form";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        return "producto-form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto,
                                  BindingResult result,
                                  Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.findAll());
            return "producto-form";
        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return "redirect:/productos";
    }
}
