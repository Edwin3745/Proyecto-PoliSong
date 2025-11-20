package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
	Optional<Proveedor> findByCorreo(String correo);
}

