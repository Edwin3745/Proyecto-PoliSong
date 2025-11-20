package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    //  Buscar todas las ventas asociadas a un proveedor por su ID
    List<Venta> findByProveedor_IdProveedor(Integer idProveedor);
}
