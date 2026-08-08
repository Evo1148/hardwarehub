package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Categoria;
import com.hardwarehub.hardwarehub.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Listado de categorías
    @GetMapping
    public String listarCategorias(Model model, @ModelAttribute("mensaje") String mensaje) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("mensaje", mensaje);
        return "categorias"; // nombre de la plantilla Thymeleaf
    }

    // Formulario para crear nueva categoría
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria-form";
    }

    // Formulario para editar una categoría existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCategoria(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id de categoría no válido: " + id));
        model.addAttribute("categoria", categoria);
        return "categoria-form";
    }

    // Guardar (crear o actualizar)
    @PostMapping("/guardar")
    public String guardarCategoria(@Valid @ModelAttribute("categoria") Categoria categoria,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "categoria-form";
        }
        categoriaService.save(categoria);
        return "redirect:/categorias";
    }

    // Eliminar
    @PostMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (categoriaService.isCategoriaEnUso(id)) {
            redirectAttributes.addFlashAttribute("mensaje", "No se puede eliminar: la categoría tiene productos asociados.");
            return "redirect:/categorias";
        }

        categoriaService.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada correctamente.");
        return "redirect:/categorias";
    }
}
