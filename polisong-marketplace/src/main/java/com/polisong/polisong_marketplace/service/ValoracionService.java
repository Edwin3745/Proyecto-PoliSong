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
    // 1️ Crear una valoración para un pedido
public Valoracion crearValoracion(Valoracion valoracion) {
    valoracion.setFecha(java.time.LocalDateTime.now());
    return valoracionRepository.save(valoracion);
}

// 2️ Actualizar comentario o calificación
public Valoracion actualizarValoracion(Integer id, Integer nuevaCalificacion, String nuevoComentario) {
    Valoracion valoracion = valoracionRepository.findById(id).orElse(null);
    if (valoracion != null) {
        valoracion.setCalificacion(nuevaCalificacion);
        valoracion.setComentario(nuevoComentario);
        return valoracionRepository.save(valoracion);
    }
    return null;
}

// 3️ Calcular promedio de calificación general
public double calcularPromedioCalificaciones() {
    List<Valoracion> valoraciones = valoracionRepository.findAll();
    if (valoraciones.isEmpty()) return 0;
    double total = valoraciones.stream().mapToDouble(Valoracion::getCalificacion).sum();
    return total / valoraciones.size();
}

// 4️ Listar valoraciones recientes (últimos 7 días)
public List<Valoracion> listarValoracionesRecientes() {
    java.time.LocalDateTime haceUnaSemana = java.time.LocalDateTime.now().minusDays(7);
    return valoracionRepository.findAll().stream()
            .filter(v -> v.getFecha().isAfter(haceUnaSemana))
            .toList();
}

}
