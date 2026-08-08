package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> findActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        return usuarioRepository.findByEmail(authentication.getName());
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        usuario.setId(null);
        usuario.setEmail(usuario.getEmail().trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);

        // Por seguridad, desde el registro público no se permite crear admins.
        if (usuario.getRol() == null || Rol.ADMIN.equals(usuario.getRol())) {
            usuario.setRol(Rol.CLIENTE);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    public List<Usuario> findVendedores() {
        return usuarioRepository.findByRol(Rol.VENDEDOR);
    }
}
