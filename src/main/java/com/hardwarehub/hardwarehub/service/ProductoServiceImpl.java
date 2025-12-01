package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> buscar(String nombre, Long categoriaId) {
        boolean tieneNombre = nombre != null && !nombre.trim().isEmpty();
        boolean tieneCategoria = categoriaId != null;

        if (tieneNombre && tieneCategoria) {
            return productoRepository
                    .findByNombreContainingIgnoreCaseAndCategoriaId(nombre.trim(), categoriaId);
        } else if (tieneNombre) {
            return productoRepository
                    .findByNombreContainingIgnoreCase(nombre.trim());
        } else if (tieneCategoria) {
            return productoRepository
                    .findByCategoriaId(categoriaId);
        } else {
            return productoRepository.findAll();
        }
    }
}
