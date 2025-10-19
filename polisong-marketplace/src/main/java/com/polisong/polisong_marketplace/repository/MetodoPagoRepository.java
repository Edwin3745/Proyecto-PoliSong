package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
}
