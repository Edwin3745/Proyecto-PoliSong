package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.service.ViniloService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vinilos")
@CrossOrigin(origins = "*")
public class ViniloController {

    private final ViniloService viniloService;

    public ViniloController(ViniloService viniloService) {
        this.viniloService = viniloService;
    }

    @GetMapping
    public List<Vinilo> listar() {
        return viniloService.listar();
    }

    @GetMapping("/{id}")
    public Vinilo obtenerPorId(@PathVariable Integer id) {
        return viniloService.buscarPorId(id);
    }

    @PostMapping
    public Vinilo guardar(@RequestBody Vinilo vinilo) {
        return viniloService.guardar(vinilo);
    }

    @PutMapping("/{id}")
    public Vinilo actualizar(@PathVariable Integer id, @RequestBody Vinilo vinilo) {
        Vinilo existente = viniloService.buscarPorId(id);
        if (existente != null) {
            vinilo.setIdVinilo(id);
            return viniloService.guardar(vinilo);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        viniloService.eliminar(id);
    }
}
