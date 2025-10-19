package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Carrito;
import com.polisong.polisong_marketplace.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public List<Carrito> listar() {
        return carritoRepository.findAll();
    }

    public Carrito buscarPorId(Integer id) {
        return carritoRepository.findById(id).orElse(null);
    }

    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public void eliminar(Integer id) {
        carritoRepository.deleteById(id);
    }
}
