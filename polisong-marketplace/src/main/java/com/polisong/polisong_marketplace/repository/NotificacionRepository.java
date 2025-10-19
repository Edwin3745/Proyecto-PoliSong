package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
