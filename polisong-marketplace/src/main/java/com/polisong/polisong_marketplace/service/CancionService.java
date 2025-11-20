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

    public List<Cancion> buscarPorNombre(String nombre) {
        return cancionRepository.findAll().stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    public List<Cancion> filtrarPorPrecio(double minimo, double maximo) {
        return cancionRepository.findAll().stream()
                .filter(c -> c.getPrecio() >= minimo && c.getPrecio() <= maximo)
                .toList();
    }

    public double calcularDuracionTotal(List<Integer> idsCanciones) {
        return idsCanciones.stream()
                .map(cancionRepository::findById)
                .filter(java.util.Optional::isPresent)
                .mapToDouble(opt -> opt.get().getDuracion())
                .sum();
    }

    public boolean verificarDisponibilidad(Integer id) {
        Cancion cancion = cancionRepository.findById(id).orElse(null);
        return cancion != null && cancion.getPrecio() != null && cancion.getDuracion() != null;
    }
}

