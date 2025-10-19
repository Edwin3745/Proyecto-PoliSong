package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Producto;
import com.polisong.polisong_marketplace.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Listar todos los productos
    @GetMapping
    public List<Producto> listarTodos() {
        return productoService.listarTodos();
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public Optional<Producto> buscarPorId(@PathVariable int id) {
        return productoService.buscarPorId(id);
    }

    // Buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public List<Producto> buscarPorNombre(@PathVariable String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    // Buscar por tipo (VINILO o MP3)
    @GetMapping("/tipo/{tipo}")
    public List<Producto> buscarPorTipo(@PathVariable String tipo) {
        return productoService.buscarPorTipo(tipo);
    }

    // Listar productos activos
    @GetMapping("/activos")
    public List<Producto> listarActivos() {
        return productoService.listarActivos();
    }

    // Crear o actualizar producto
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        productoService.eliminar(id);
    }
}
