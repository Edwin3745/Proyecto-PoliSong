package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Reporte;
import com.polisong.polisong_marketplace.service.ReporteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public List<Reporte> listar() {
        return reporteService.listar();
    }

    @GetMapping("/{id}")
    public Reporte obtenerPorId(@PathVariable Integer id) {
        return reporteService.buscarPorId(id);
    }

    @PostMapping
    public Reporte guardar(@RequestBody Reporte reporte) {
        return reporteService.guardar(reporte);
    }

    @PutMapping("/{id}")
    public Reporte actualizar(@PathVariable Integer id, @RequestBody Reporte reporte) {
        Reporte existente = reporteService.buscarPorId(id);
        if (existente != null) {
            reporte.setIdReporte(id);
            return reporteService.guardar(reporte);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        reporteService.eliminar(id);
    }
}
