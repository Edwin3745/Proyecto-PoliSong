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
    private ViniloRepository viniloRepository;

    @Autowired
    private CancionRepository cancionRepository;




public Pedido registrarCompra(Integer idUsuario, List<DetallePedido> detalles, Integer idMetodoPago) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
    Optional<MetodoPago> metodoOpt = metodoPagoRepository.findById(idMetodoPago);

    if (usuarioOpt.isEmpty() || metodoOpt.isEmpty()) {
        throw new IllegalArgumentException("Usuario o método de pago inválido");
    }

    Pedido pedido = new Pedido();
    pedido.setUsuario(usuarioOpt.get());
    pedido.setMetodoPago(metodoOpt.get());
    // usa el campo 'fecha' que tienes en Pedido (LocalDateTime)
    pedido.setFecha(LocalDateTime.now());
    pedido.setEstado("Pendiente");

    double total = 0.0;

    // Asegúrate de inicializar la lista de detalles en el pedido
    // (opcional: pedido.setDetalles(new ArrayList<>());)
    for (DetallePedido detalle : detalles) {
        // Asocia el detalle al pedido
        detalle.setPedido(pedido);

        // Si el producto es un vinilo
        if (detalle.getVinilo() != null && detalle.getVinilo().getIdVinilo() != null) {
            Optional<Vinilo> vOpt = viniloRepository.findById(detalle.getVinilo().getIdVinilo());
            if (vOpt.isPresent()) {
                Vinilo v = vOpt.get();
                detalle.setPrecioUnitario(v.getPrecio());
                detalle.setSubtotal(v.getPrecio() * detalle.getCantidad());
                total += detalle.getSubtotal();
            } else {
                throw new IllegalArgumentException("Vinilo no encontrado: " + detalle.getVinilo().getIdVinilo());
            }
        }

        // Si el producto es una canción
        if (detalle.getCancion() != null && detalle.getCancion().getIdCancion() != null) {
            Optional<Cancion> cOpt = cancionRepository.findById(detalle.getCancion().getIdCancion());
            if (cOpt.isPresent()) {
                Cancion c = cOpt.get();
                detalle.setPrecioUnitario(c.getPrecio());
                detalle.setSubtotal(c.getPrecio() * detalle.getCantidad());
                total += detalle.getSubtotal();
            } else {
                throw new IllegalArgumentException("Cancion no encontrada: " + detalle.getCancion().getIdCancion());
            }
        }
    }

    // guarda subtotal/total en el pedido (si tienes subtotal puedes setearlo también)
    pedido.setTotal(total);

    // Primero guarda el pedido (para que tenga id) y luego los detalles
    pedido = pedidoRepository.save(pedido);

    // ahora cada detalle tiene pedido (con id) y se pueden guardar
    detallePedidoRepository.saveAll(detalles);

    return pedido;
}


    
    public Pedido consultarCompra(Integer idPedido) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el pedido con ID: " + idPedido);
        }
        return pedidoOpt.get();
    }

    
    public List<Pedido> listarComprasUsuario(Integer idUsuario) {
    return pedidoRepository.findByUsuario_IdUsuario(idUsuario);
}


    
    public String generarFactura(Integer idPedido) {
        Pedido pedido = consultarCompra(idPedido);
        StringBuilder factura = new StringBuilder();

        factura.append("======== FACTURA POLISONG ========\n");
        factura.append("Cliente: ").append(pedido.getUsuario().getNombre()).append("\n");
        factura.append("Correo: ").append(pedido.getUsuario().getCorreo()).append("\n");
        factura.append("Fecha: ").append(pedido.getFecha()).append("\n");
        factura.append("----------------------------------\n");

        double total = 0.0;
        List<DetallePedido> detalles = detallePedidoRepository.findByPedido_IdPedido(idPedido);
        for (DetallePedido d : detalles) {
            String nombreProducto = "";
            if (d.getVinilo() != null) nombreProducto = d.getVinilo().getNombre();
            if (d.getCancion() != null) nombreProducto = d.getCancion().getNombreCancion();

            factura.append(nombreProducto)
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

