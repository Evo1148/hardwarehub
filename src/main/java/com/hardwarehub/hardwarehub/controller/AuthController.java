package com.hardwarehub.hardwarehub.controller;

import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("rolesRegistro", List.of(Rol.CLIENTE, Rol.VENDEDOR));
        return "registro";
    }

    @PostMapping("/registro")
    public String guardarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        if (usuario.getRol() == null || Rol.ADMIN.equals(usuario.getRol())) {
            usuario.setRol(Rol.CLIENTE);
        }

        if (usuario.getEmail() != null && usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "email.repetido", "Ya existe un usuario con ese email");
        }

        if (result.hasErrors()) {
            model.addAttribute("rolesRegistro", List.of(Rol.CLIENTE, Rol.VENDEDOR));
            return "registro";
        }

        usuarioService.registrar(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado correctamente. Ya puedes iniciar sesión.");
        return "redirect:/login";
    }
}
