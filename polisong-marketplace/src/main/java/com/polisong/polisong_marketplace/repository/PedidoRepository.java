package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuario_IdUsuario(Integer idUsuario);
}

