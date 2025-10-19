package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Integer> {
}
