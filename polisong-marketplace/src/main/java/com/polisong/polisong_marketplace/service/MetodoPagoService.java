package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.MetodoPago;
import com.polisong.polisong_marketplace.repository.MetodoPagoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public MetodoPagoService(MetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public List<MetodoPago> listar() {
        return metodoPagoRepository.findAll();
    }

    public MetodoPago buscarPorId(Integer id) {
        return metodoPagoRepository.findById(id).orElse(null);
    }

    public MetodoPago guardar(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    public void eliminar(Integer id) {
        metodoPagoRepository.deleteById(id);
    }
}
