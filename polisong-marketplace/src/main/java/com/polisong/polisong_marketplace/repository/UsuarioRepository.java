package com.polisong.polisong_marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.polisong.polisong_marketplace.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCedula(String cedula);
}
