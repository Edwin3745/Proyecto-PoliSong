package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.*;
import com.polisong.polisong_marketplace.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private ProductoRepository productoRepository; //  Nuevo repositorio genérico para Producto


    public Pedido registrarCompra(Integer idUsuario, List<DetallePedido> detalles, Integer idMetodoPago) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        Optional<MetodoPago> metodoOpt = metodoPagoRepository.findById(idMetodoPago);

        if (usuarioOpt.isEmpty() || metodoOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario o método de pago inválido");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuarioOpt.get());
        pedido.setMetodoPago(metodoOpt.get());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("Pendiente");

        double total = 0.0;

        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedido);

            Producto producto = detalle.getProducto();

            if (producto != null && producto.getIdProducto() != null) {
                Producto p = productoRepository.findById(producto.getIdProducto())
                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

                detalle.setPrecioUnitario(p.getPrecio());
                detalle.setSubtotal(p.getPrecio() * detalle.getCantidad());
                total += detalle.getSubtotal();
            } else {
                throw new IllegalArgumentException("Detalle sin producto válido");
            }
        }

        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        detallePedidoRepository.saveAll(detalles);

        return pedidoGuardado;
    }


    public Pedido consultarCompra(Integer idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada con ID: " + idPedido));
    }


    public List<Pedido> listarComprasUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuario_IdUsuario(idUsuario);
    }


    public String generarFactura(Integer idPedido) {
    Pedido pedido = consultarCompra(idPedido);
    List<DetallePedido> detalles = detallePedidoRepository.findByPedido_IdPedido(idPedido);

    StringBuilder factura = new StringBuilder();
    factura.append("======== FACTURA POLISONG ========\n");
    factura.append("Cliente: ").append(pedido.getUsuario().getNombreUsuario()).append("\n");
    factura.append("Correo: ").append(pedido.getUsuario().getCorreoPrincipal()).append("\n");
    factura.append("Fecha: ").append(pedido.getFecha()).append("\n");
    factura.append("----------------------------------\n");

    double total = 0.0;

    for (DetallePedido d : detalles) {
        Producto producto = d.getProducto();

        factura.append(producto.getNombre())
               .append("  x")
               .append(d.getCantidad())
               .append("  $")
               .append(d.getPrecioUnitario())
               .append("\n");

        total += d.getSubtotal();
    }

    factura.append("----------------------------------\n");
    factura.append("TOTAL: $").append(total).append("\n");
    factura.append("Método de pago: ").append(pedido.getMetodoPago().getNombre()).append("\n");
    factura.append("==================================");

    return factura.toString();
}
}



