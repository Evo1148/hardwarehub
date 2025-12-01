package com.hardwarehub.hardwarehub.repository;

import com.hardwarehub.hardwarehub.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);
}
