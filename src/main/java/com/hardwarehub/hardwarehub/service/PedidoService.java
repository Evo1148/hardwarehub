package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import com.hardwarehub.hardwarehub.model.LineaPedido;
import com.hardwarehub.hardwarehub.model.Pedido;
import com.hardwarehub.hardwarehub.model.Usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PedidoService {

    Pedido finalizarCompra(Map<Long, CarritoItem> carrito, Usuario comprador);

    List<Pedido> findPedidosComprador(Long compradorId);

    Optional<Pedido> findById(Long id);

    List<LineaPedido> findVentasVendedor(Long vendedorId);

    List<LineaPedido> findTodasLasVentas();
}
