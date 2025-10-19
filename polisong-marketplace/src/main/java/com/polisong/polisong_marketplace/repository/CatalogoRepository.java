package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
}
