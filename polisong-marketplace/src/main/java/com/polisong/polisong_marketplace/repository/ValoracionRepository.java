package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Integer> {
}
