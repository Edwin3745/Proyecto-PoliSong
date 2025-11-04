package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.MetodoPago;
import com.polisong.polisong_marketplace.service.MetodoPagoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@CrossOrigin(origins = "*")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    public List<MetodoPago> listar() {
        return metodoPagoService.listar();
    }

    @GetMapping("/{id}")
    public MetodoPago obtenerPorId(@PathVariable Integer id) {
        return metodoPagoService.buscarPorId(id);
    }

    @PostMapping
    public MetodoPago guardar(@RequestBody MetodoPago metodoPago) {
        return metodoPagoService.guardar(metodoPago);
    }


    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        metodoPagoService.eliminar(id);
    }
}
