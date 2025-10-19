package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.repository.ViniloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViniloService {

    @Autowired
    private ViniloRepository productoRepository;

    public List<Vinilo> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Vinilo> buscarPorId(int idProducto) {
        return productoRepository.findById(idProducto);
    }

    public Vinilo guardar(Vinilo producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(int idProducto) {
        productoRepository.deleteById(idProducto);
    }

    public List<Vinilo> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Vinilo> buscarPorTipo(String tipo) {
        return productoRepository.findByTipo(tipo);
    }

    public List<Vinilo> listarActivos() {
        return productoRepository.findByEstado("Activo");
    }
}
