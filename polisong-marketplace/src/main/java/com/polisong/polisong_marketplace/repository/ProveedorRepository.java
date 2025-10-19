package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
