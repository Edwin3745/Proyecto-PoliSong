package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Notificacion;
import com.polisong.polisong_marketplace.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByUsuario(Usuario usuario);
    List<Notificacion> findByEstado(String estado);
}
