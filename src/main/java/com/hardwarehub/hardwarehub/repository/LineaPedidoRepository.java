package com.hardwarehub.hardwarehub.repository;

import com.hardwarehub.hardwarehub.model.LineaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long> {

    @Query("select l from LineaPedido l where l.vendedor.id = :vendedorId order by l.pedido.fecha desc")
    List<LineaPedido> findVentasByVendedor(@Param("vendedorId") Long vendedorId);

    @Query("select l from LineaPedido l order by l.pedido.fecha desc")
    List<LineaPedido> findTodasLasVentas();
}
