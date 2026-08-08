package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.CategoriaService;
import com.hardwarehub.hardwarehub.service.ProductoService;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    public ProductoController(ProductoService productoService,
                              CategoriaService categoriaService,
                              UsuarioService usuarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        Usuario usuario = usuarioActual();

        if (Rol.ADMIN.equals(usuario.getRol())) {
            model.addAttribute("productos", productoService.findAll());
        } else {
            model.addAttribute("productos", productoService.findByVendedorId(usuario.getId()));
        }

        return "productos";
    }

    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        cargarDatosFormulario(model);
        return "producto-form";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

        if (!puedeGestionar(producto)) {
            return "redirect:/productos";
        }

        model.addAttribute("producto", producto);
        cargarDatosFormulario(model);
        return "producto-form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioActual();

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "producto-form";
        }

        if (producto.getId() != null) {
            Producto productoExistente = productoService.findById(producto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

            if (!puedeGestionar(productoExistente)) {
                return "redirect:/productos";
            }

            // Si es vendedor, no puede cambiar el propietario del producto.
            if (!Rol.ADMIN.equals(usuario.getRol())) {
                producto.setVendedor(productoExistente.getVendedor());
            }
        } else {
            // Si es vendedor, el producto queda asociado automáticamente a él.
            if (!Rol.ADMIN.equals(usuario.getRol())) {
                producto.setVendedor(usuario);
            }
        }

        if (producto.getVendedor() == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes asignar un vendedor al producto.");
            return "redirect:/productos/nuevo";
        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido"));

        if (!puedeGestionar(producto)) {
            redirectAttributes.addFlashAttribute("mensaje", "No tienes permiso para eliminar ese producto.");
            return "redirect:/productos";
        }

        try {
            productoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado correctamente.");
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "No se puede eliminar porque ya aparece en alguna venta. Puedes dejarlo sin stock si no quieres venderlo más.");
        }

        return "redirect:/productos";
    }

    private Usuario usuarioActual() {
        return usuarioService.findActual()
                .orElseThrow(() -> new IllegalArgumentException("Debes iniciar sesión."));
    }

    private boolean puedeGestionar(Producto producto) {
        Usuario usuario = usuarioActual();

        if (Rol.ADMIN.equals(usuario.getRol())) {
            return true;
        }

        return producto.getVendedor() != null && producto.getVendedor().getId().equals(usuario.getId());
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("vendedores", usuarioService.findVendedores());
    }
}
