package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByProveedor_IdProveedor(Integer idProveedor);
}
