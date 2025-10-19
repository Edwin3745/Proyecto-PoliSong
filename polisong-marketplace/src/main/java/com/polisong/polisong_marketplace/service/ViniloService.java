package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.repository.ViniloRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViniloService {

    private final ViniloRepository viniloRepository;

    public ViniloService(ViniloRepository viniloRepository) {
        this.viniloRepository = viniloRepository;
    }

    public List<Vinilo> listar() {
        return viniloRepository.findAll();
    }

    public Vinilo buscarPorId(Integer id) {
        return viniloRepository.findById(id).orElse(null);
    }

    public Vinilo guardar(Vinilo vinilo) {
        return viniloRepository.save(vinilo);
    }

    public void eliminar(Integer id) {
        viniloRepository.deleteById(id);
    }
}

