package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Cancion;
import com.polisong.polisong_marketplace.repository.CancionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CancionService {

    private final CancionRepository cancionRepository;

    public CancionService(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    public List<Cancion> listar() {
        return cancionRepository.findAll();
    }

    public Cancion buscarPorId(Integer id) {
        return cancionRepository.findById(id).orElse(null);
    }

    public Cancion guardar(Cancion cancion) {
        return cancionRepository.save(cancion);
    }

    public void eliminar(Integer id) {
        cancionRepository.deleteById(id);
    }
    
}
