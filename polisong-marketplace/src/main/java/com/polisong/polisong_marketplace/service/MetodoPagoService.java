package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.MetodoPago;
import com.polisong.polisong_marketplace.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    // Registrar método de pago
   
    public MetodoPago registrarMetodoPago(MetodoPago metodoPago) {
        if (metodoPago == null || metodoPago.getNombre() == null || metodoPago.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del método de pago es obligatorio");
        }
        return metodoPagoRepository.save(metodoPago);
    }

    // Actualizar método de pago
    
    public MetodoPago actualizarMetodoPago(Integer id, MetodoPago metodoActualizado) {
        Optional<MetodoPago> metodoOpt = metodoPagoRepository.findById(id);

        if (metodoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el método de pago con ID: " + id);
        }

        MetodoPago metodo = metodoOpt.get();
        metodo.setNombre(metodoActualizado.getNombre());
        metodo.setDescripcion(metodoActualizado.getDescripcion());
        metodo.setActivo(metodoActualizado.getActivo());

        return metodoPagoRepository.save(metodo);
    }

    // Consultar método de pago
    
    public MetodoPago consultarMetodoPago(Integer id) {
        return metodoPagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el método de pago con ID: " + id));
    }

    // Listar métodos activos
    
    public List<MetodoPago> listarMetodosActivos() {
        return metodoPagoRepository.findAll()
                .stream()
                .filter(MetodoPago::getActivo)
                .toList();
    }

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

