package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Reporte;
import com.polisong.polisong_marketplace.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<Reporte> listar() {
        return reporteRepository.findAll();
    }

    public Reporte buscarPorId(Integer id) {
        return reporteRepository.findById(id).orElse(null);
    }

    public Reporte guardar(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminar(Integer id) {
        reporteRepository.deleteById(id);
    }
    // Listar reportes por usuario (administrador) usando la FK del usuario
    public List<Reporte> listarPorUsuario(Integer idUsuario) {
        return reporteRepository.findByAdministrador_IdAdmin(idUsuario);
    }

    // Listar reportes por "estado
    // Mantenemos el nombre para compatibilidad
    public List<Reporte> listarPorEstado(String estadoOTipo) {
        return reporteRepository.findByTipoIgnoreCase(estadoOTipo);
    }

    // Actualizar el "estado" del reporte
    public Reporte actualizarEstado(Integer idReporte, String nuevoEstadoOTipo) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(idReporte);
        if (reporteOpt.isPresent()) {
            Reporte reporte = reporteOpt.get();
            reporte.setTipo(nuevoEstadoOTipo);
            return reporteRepository.save(reporte);
        }
        return null;
    }

    // Contar la cantidad de reportes creados en una fecha
    public long contarPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay(); 
        return reporteRepository.countByFechaGeneracionBetween(inicio, fin);
    }
}
