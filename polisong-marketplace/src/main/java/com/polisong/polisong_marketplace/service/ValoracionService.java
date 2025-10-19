package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Valoracion;
import com.polisong.polisong_marketplace.repository.ValoracionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ValoracionService {

    private final ValoracionRepository valoracionRepository;

    public ValoracionService(ValoracionRepository valoracionRepository) {
        this.valoracionRepository = valoracionRepository;
    }

    public List<Valoracion> listar() {
        return valoracionRepository.findAll();
    }

    public Valoracion buscarPorId(Integer id) {
        return valoracionRepository.findById(id).orElse(null);
    }

    public Valoracion guardar(Valoracion valoracion) {
        return valoracionRepository.save(valoracion);
    }

    public void eliminar(Integer id) {
        valoracionRepository.deleteById(id);
    }
}
