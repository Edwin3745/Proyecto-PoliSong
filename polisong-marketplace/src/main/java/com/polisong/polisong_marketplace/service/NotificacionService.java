package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Notificacion;
import com.polisong.polisong_marketplace.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

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
}
