package com.hardwarehub.hardwarehub.repository;

import com.hardwarehub.hardwarehub.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCompradorIdOrderByFechaDesc(Long compradorId);
}
