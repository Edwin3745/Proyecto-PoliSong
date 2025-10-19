package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Vinilo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViniloRepository extends JpaRepository<Vinilo, Integer> {
}
