package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.Rol;
import com.hardwarehub.hardwarehub.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findActual();

    Usuario registrar(Usuario usuario);

    boolean existsByEmail(String email);

    List<Usuario> findVendedores();
}
