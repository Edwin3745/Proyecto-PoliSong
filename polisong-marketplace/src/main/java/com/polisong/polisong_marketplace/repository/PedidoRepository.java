package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
