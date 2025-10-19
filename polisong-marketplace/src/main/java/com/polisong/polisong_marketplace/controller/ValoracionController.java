package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Valoracion;
import com.polisong.polisong_marketplace.service.ValoracionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/valoraciones")
@CrossOrigin(origins = "*")
public class ValoracionController {

    private final ValoracionService valoracionService;

    public ValoracionController(ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @GetMapping
    public List<Valoracion> listar() {
        return valoracionService.listar();
    }

    @GetMapping("/{id}")
    public Valoracion obtenerPorId(@PathVariable Integer id) {
        return valoracionService.buscarPorId(id);
    }

    @PostMapping
    public Valoracion guardar(@RequestBody Valoracion valoracion) {
        return valoracionService.guardar(valoracion);
    }

    @PutMapping("/{id}")
    public Valoracion actualizar(@PathVariable Integer id, @RequestBody Valoracion valoracion) {
        Valoracion existente = valoracionService.buscarPorId(id);
        if (existente != null) {
            valoracion.setIdValoracion(id);
            return valoracionService.guardar(valoracion);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        valoracionService.eliminar(id);
    }
}
