package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Notificacion;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    //  MÉTODOS BÁSICOS

    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    public Notificacion buscarPorId(Integer id) {
        return notificacionRepository.findById(id).orElse(null);
    }

    public Notificacion guardar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public void eliminar(Integer id) {
        notificacionRepository.deleteById(id);
    }
   
    // MÉTODOS FUNCIONALES

    // 1️ Enviar una notificación genérica
    public void enviarNotificacion(Usuario usuario, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setMensaje(mensaje);
        notificacion.setEstado("No leída");
        notificacionRepository.save(notificacion);
        System.out.println("📩 Notificación enviada a: " + usuario.getNombre());
    }

    // 2️ Notificar cambio en pedido
    public void notificarCambioPedido(Usuario usuario, String estadoPedido) {
        String mensaje = "El estado de tu pedido ha cambiado a: " + estadoPedido;
        enviarNotificacion(usuario, mensaje);
    }

    // 3️ Notificar promoción
    public void notificarPromocion(Usuario usuario, String descripcionPromo) {
        String mensaje = "Nueva promoción disponible: " + descripcionPromo;
        enviarNotificacion(usuario, mensaje);
    }

    // 4️ Enviar correo de notificación (simulado)
    public void enviarCorreoNotificacion(String email, String asunto, String mensaje) {
        System.out.println("Enviando correo a: " + email);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }
}
