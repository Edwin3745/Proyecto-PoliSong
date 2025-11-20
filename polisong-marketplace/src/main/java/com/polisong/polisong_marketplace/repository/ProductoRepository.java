// ProductoRepository.java
package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> { }

