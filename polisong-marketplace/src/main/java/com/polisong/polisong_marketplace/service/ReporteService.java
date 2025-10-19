package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Reporte;
import com.polisong.polisong_marketplace.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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
}
