package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Carrito;
import com.polisong.polisong_marketplace.service.CarritoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public List<Carrito> listar() {
        return carritoService.listar();
    }

    @GetMapping("/{id}")
    public Carrito obtenerPorId(@PathVariable Integer id) {
        return carritoService.buscarPorId(id);
    }

    @PostMapping
    public Carrito guardar(@RequestBody Carrito carrito) {
        return carritoService.guardar(carrito);
    }

    @PutMapping("/{id}")
    public Carrito actualizar(@PathVariable Integer id, @RequestBody Carrito carrito) {
        Carrito existente = carritoService.buscarPorId(id);
        if (existente != null) {
            carrito.setIdCarrito(id);
            return carritoService.guardar(carrito);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        carritoService.eliminar(id);
    }
}
