package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar productos por nombre o tipo
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByTipo(String tipo);

    // Filtrar productos activos
    List<Producto> findByEstado(String estado);
}
