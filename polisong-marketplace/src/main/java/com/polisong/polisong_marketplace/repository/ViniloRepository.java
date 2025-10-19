package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Vinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViniloRepository extends JpaRepository<Vinilo, Integer> {

    // Buscar productos por nombre o tipo
    List<Vinilo> findByNombreContainingIgnoreCase(String nombre);
    List<Vinilo> findByTipo(String tipo);

    // Filtrar productos activos
    List<Vinilo> findByEstado(String estado);
}
