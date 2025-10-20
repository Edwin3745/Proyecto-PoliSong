package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.model.Cancion;
import com.polisong.polisong_marketplace.model.Notificacion;
import com.polisong.polisong_marketplace.repository.PedidoRepository;
import com.polisong.polisong_marketplace.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final NotificacionRepository notificacionRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, NotificacionRepository notificacionRepository) {
        this.pedidoRepository = pedidoRepository;
        this.notificacionRepository = notificacionRepository;
    }

    // Listar todos los pedidos
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // Buscar pedido por ID 
    public Pedido buscarPorId(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    // Guardar o actualizar un pedido
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    // Eliminar pedido
    public void eliminar(Integer id) {
        pedidoRepository.deleteById(id);
    }

   //Calcular total (Depende si DetallePedido tiene vinilos o canciones, de lo contrario el total sera 0)
    public double calcularTotal(Pedido pedido) {
        double total = 0.0;

        if (pedido.getDetalles() != null) {
            total = pedido.getDetalles()
                    .stream()
                    .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
                    .sum();
        }

        return total;
    }
    // Actualizar estado (ENVIADO, ENTREGADO)
    public Pedido actualizarEstado(Integer id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setObservacion("Estado actualizado a: " + nuevoEstado);
        Pedido actualizado = pedidoRepository.save(pedido);

        // Notificación
        enviarNotificacionCambioEstado(actualizado,
                "El estado de tu pedido cambió a: " + nuevoEstado);

        return actualizado;
    }
    // Cancelar pedido (si no fue enviado o entregado)
    public Pedido cancelarPedido(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setObservacion("Pedido cancelado por el usuario.");
        Pedido cancelado = pedidoRepository.save(pedido);

        enviarNotificacionCambioEstado(cancelado, "Tu pedido fue cancelado correctamente.");
        return cancelado;
    }
    // Notificación automática por cambio de estado
    private void enviarNotificacionCambioEstado(Pedido pedido, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(pedido.getUsuario());
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}
