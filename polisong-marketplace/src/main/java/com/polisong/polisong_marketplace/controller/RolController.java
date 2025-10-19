package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Rol;
import com.polisong.polisong_marketplace.service.RolService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public Rol obtenerPorId(@PathVariable Integer id) {
        return rolService.buscarPorId(id);
    }

    @PostMapping
    public Rol guardar(@RequestBody Rol rol) {
        return rolService.guardar(rol);
    }

    @PutMapping("/{id}")
    public Rol actualizar(@PathVariable Integer id, @RequestBody Rol rol) {
        Rol existente = rolService.buscarPorId(id);
        if (existente != null) {
            rol.setIdRol(id);
            return rolService.guardar(rol);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        rolService.eliminar(id);
    }
}
