package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    List<Producto> findAll();

    List<Producto> findByVendedorId(Long vendedorId);

    Optional<Producto> findById(Long id);

    Producto save(Producto producto);

    void deleteById(Long id);

    List<Producto> buscar(String nombre, Long categoriaId);
}
