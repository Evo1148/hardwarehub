package com.hardwarehub.hardwarehub.service;

import com.hardwarehub.hardwarehub.model.CarritoItem;
import com.hardwarehub.hardwarehub.model.LineaPedido;
import com.hardwarehub.hardwarehub.model.Pedido;
import com.hardwarehub.hardwarehub.model.Producto;
import com.hardwarehub.hardwarehub.model.Usuario;
import com.hardwarehub.hardwarehub.repository.LineaPedidoRepository;
import com.hardwarehub.hardwarehub.repository.PedidoRepository;
import com.hardwarehub.hardwarehub.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final LineaPedidoRepository lineaPedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ProductoRepository productoRepository,
                             LineaPedidoRepository lineaPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
    }

    @Override
    public Pedido finalizarCompra(Map<Long, CarritoItem> carrito, Usuario comprador) {
        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío.");
        }

        Pedido pedido = new Pedido(comprador);
        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem item : carrito.values()) {
            Long productoId = item.getProducto().getId();
            int cantidad = item.getCantidad();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("El producto ya no existe."));

            if (producto.getVendedor() == null) {
                throw new IllegalArgumentException("El producto " + producto.getNombre() + " no tiene vendedor asignado.");
            }

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
            }

            if (producto.getStock() < cantidad) {
                throw new IllegalArgumentException("No hay stock suficiente de " + producto.getNombre() + ".");
            }

            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            LineaPedido linea = new LineaPedido(producto, producto.getVendedor(), cantidad);
            pedido.addLinea(linea);
            total = total.add(linea.getSubtotal());
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findPedidosComprador(Long compradorId) {
        return pedidoRepository.findByCompradorIdOrderByFechaDesc(compradorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LineaPedido> findVentasVendedor(Long vendedorId) {
        return lineaPedidoRepository.findVentasByVendedor(vendedorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LineaPedido> findTodasLasVentas() {
        return lineaPedidoRepository.findTodasLasVentas();
    }
}
