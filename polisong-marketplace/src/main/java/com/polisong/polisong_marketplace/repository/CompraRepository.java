package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Pedido, Integer> {

    // Busca pedidos por el ID del usuario (nuevo campo)
    List<Pedido> findByUsuario_IdUsuario(Integer idUsuario);
}
