package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Catalogo;
import com.polisong.polisong_marketplace.service.CatalogoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = "*")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public List<Catalogo> listar() {
        return catalogoService.listar();
    }

    @GetMapping("/{id}")
    public Catalogo obtenerPorId(@PathVariable Integer id) {
        return catalogoService.buscarPorId(id);
    }

    @PostMapping
    public Catalogo guardar(@RequestBody Catalogo catalogo) {
        return catalogoService.guardar(catalogo);
    }

    @PutMapping("/{id}")
    public Catalogo actualizar(@PathVariable Integer id, @RequestBody Catalogo catalogo) {
        Catalogo existente = catalogoService.buscarPorId(id);
        if (existente != null) {
            catalogo.setIdCatalogo(id);
            return catalogoService.guardar(catalogo);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        catalogoService.eliminar(id);
    }
}
