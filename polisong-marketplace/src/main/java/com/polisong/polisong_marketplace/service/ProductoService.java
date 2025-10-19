package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Producto;
import com.polisong.polisong_marketplace.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(int idProducto) {
        return productoRepository.findById(idProducto);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(int idProducto) {
        productoRepository.deleteById(idProducto);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorTipo(String tipo) {
        return productoRepository.findByTipo(tipo);
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByEstado("Activo");
    }
}
