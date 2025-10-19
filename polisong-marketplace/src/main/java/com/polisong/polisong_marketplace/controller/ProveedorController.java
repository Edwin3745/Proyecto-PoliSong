package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Proveedor;
import com.polisong.polisong_marketplace.service.ProveedorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    public Proveedor obtenerPorId(@PathVariable Integer id) {
        return proveedorService.buscarPorId(id);
    }

    @PostMapping
    public Proveedor guardar(@RequestBody Proveedor proveedor) {
        return proveedorService.guardar(proveedor);
    }

    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable Integer id, @RequestBody Proveedor proveedor) {
        Proveedor existente = proveedorService.buscarPorId(id);
        if (existente != null) {
            proveedor.setIdProveedor(id);
            return proveedorService.guardar(proveedor);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        proveedorService.eliminar(id);
    }
}
