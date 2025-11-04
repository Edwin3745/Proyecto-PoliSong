package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Notificacion;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.service.NotificacionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<Notificacion> listar() {
        return notificacionService.listar();
    }

    @GetMapping("/{id}")
    public Notificacion obtenerPorId(@PathVariable Integer id) {
        return notificacionService.buscarPorId(id);
    }

    @PostMapping
    public Notificacion guardar(@RequestBody Notificacion notificacion) {
        return notificacionService.guardar(notificacion);
    }

    @PutMapping("/{id}")
    public Notificacion actualizar(@PathVariable Integer id, @RequestBody Notificacion notificacion) {
        Notificacion existente = notificacionService.buscarPorId(id);
        if (existente != null) {
            notificacion.setIdNotificacion(id);
            return notificacionService.guardar(notificacion);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        notificacionService.eliminar(id);
    }
    //Enviar notificacion
     @PostMapping("/enviar")
    public String enviarNotificacion(@RequestBody Usuario usuario, @RequestParam String mensaje) {
        notificacionService.enviarNotificacion(usuario, mensaje);
        return "Notificación enviada a " + usuario.getNombre();
    }

    // Notificar cambio de pedido
    @PostMapping("/pedido")
    public String notificarCambioPedido(@RequestBody Usuario usuario, @RequestParam String estado) {
        notificacionService.notificarCambioPedido(usuario, estado);
        return "Notificación de cambio de pedido enviada.";
    }

    // Notificar promocion
    @PostMapping("/promocion")
    public String notificarPromocion(@RequestBody Usuario usuario, @RequestParam String descripcion) {
        notificacionService.notificarPromocion(usuario, descripcion);
        return "Promoción notificada a " + usuario.getNombre();
    }

    // Simular envio de correo
    @PostMapping("/correo")
    public String enviarCorreo(@RequestParam String email,
                               @RequestParam String asunto,
                               @RequestParam String mensaje) {
        notificacionService.enviarCorreoNotificacion(email, asunto, mensaje);
        return "Correo enviado correctamente (simulado).";
    }
}
