package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Reporte;
import com.polisong.polisong_marketplace.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
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
    //Listar reportes por usuario
    public List<Reporte> listarPorUsuario(Integer idUsuario) {
        return reporteRepository.findByIdUsuario(idUsuario);
    }

    // Listar reportes por estado (pendiente, en revisión, resuelto)
    public List<Reporte> listarPorEstado(String estado) {
        return reporteRepository.findByEstadoIgnoreCase(estado);
    }

    // Actualizar el estado de un reporte (por ejemplo, de “pendiente” a “resuelto”)
    public Reporte actualizarEstado(Integer idReporte, String nuevoEstado) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(idReporte);
        if (reporteOpt.isPresent()) {
            Reporte reporte = reporteOpt.get();
            reporte.setEstado(nuevoEstado);
            return reporteRepository.save(reporte);
        }
        return null;
    }

    //Contar la cantidad de reportes creados en una fecha específica
    public long contarPorFecha(LocalDate fecha) {
        return reporteRepository.countByFechaCreacion(fecha);
    }
}
