package com.hardwarehub.hardwarehub.repository;

import com.hardwarehub.hardwarehub.model.Producto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Correccion posterior asistida por IA: reserva la fila de stock durante el checkout.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.id = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Long id);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);

    List<Producto> findByVendedorId(Long vendedorId);

    long countByCategoriaId(Long categoriaId);
}
