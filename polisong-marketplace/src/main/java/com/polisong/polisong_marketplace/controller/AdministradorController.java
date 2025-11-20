package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Administrador;
import com.polisong.polisong_marketplace.service.AdministradorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public List<Administrador> listar() {
        return administradorService.listar();
    }

    @GetMapping("/{id}")
    public Administrador obtenerPorId(@PathVariable Integer id) {
        return administradorService.buscarPorId(id);
    }

    @PostMapping
    public Administrador guardar(@RequestBody Administrador administrador) {
        return administradorService.guardar(administrador);
    }

    @PutMapping("/{id}")
    public Administrador actualizar(@PathVariable Integer id, @RequestBody Administrador administrador) {
        Administrador existente = administradorService.buscarPorId(id);
        if (existente != null) {
            administrador.setIdAdmin(id);
            return administradorService.guardar(administrador);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        administradorService.eliminar(id);
    }
}
