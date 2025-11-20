package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Cancion;
import com.polisong.polisong_marketplace.service.CancionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/canciones")
@CrossOrigin(origins = "*")
public class CancionController {

    private final CancionService cancionService;

    public CancionController(CancionService cancionService) {
        this.cancionService = cancionService;
    }

    @GetMapping
    public List<Cancion> listar() {
        return cancionService.listar();
    }

    @GetMapping("/{id}")
    public Cancion obtenerPorId(@PathVariable Integer id) {
        return cancionService.buscarPorId(id);
    }

    @PostMapping
    public Cancion guardar(@RequestBody Cancion cancion) {
        return cancionService.guardar(cancion);
    }

    @PutMapping("/{id}")
    public Cancion actualizar(@PathVariable Integer id, @RequestBody Cancion cancion) {
        Cancion existente = cancionService.buscarPorId(id);
        if (existente != null) {
            cancion.setIdProducto(id); // ID heredado de Producto
            return cancionService.guardar(cancion);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        cancionService.eliminar(id);
    }

    // 🔍 Métodos específicos
    @GetMapping("/buscar")
    public List<Cancion> buscarPorNombre(@RequestParam String nombre) {
        return cancionService.buscarPorNombre(nombre);
    }

    @GetMapping("/filtrar/precio")
    public List<Cancion> filtrarPorPrecio(
            @RequestParam double minimo,
            @RequestParam double maximo) {
        return cancionService.filtrarPorPrecio(minimo, maximo);
    }

    @GetMapping("/duracion/total")
    public double calcularDuracionTotal(@RequestParam List<Integer> ids) {
        return cancionService.calcularDuracionTotal(ids);
    }

    @GetMapping("/{id}/disponible")
    public boolean verificarDisponibilidad(@PathVariable Integer id) {
        return cancionService.verificarDisponibilidad(id);
    }
}

