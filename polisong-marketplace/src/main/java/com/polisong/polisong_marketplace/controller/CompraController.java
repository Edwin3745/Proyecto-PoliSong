// controller - CompraController.java
package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.DetallePedido;
import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.service.CompraService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping("/registrar")
    public Pedido registrarCompra(
            @RequestParam Integer idUsuario,
            @RequestParam Integer idMetodoPago,
            @RequestBody List<DetallePedido> detalles) {
        return compraService.registrarCompra(idUsuario, detalles, idMetodoPago);
    }

    @GetMapping("/{id}")
    public Pedido consultarCompra(@PathVariable Integer id) {
        return compraService.consultarCompra(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Pedido> listarComprasUsuario(@PathVariable Integer idUsuario) {
        return compraService.listarComprasUsuario(idUsuario);
    }

    @GetMapping("/{id}/factura")
    public String generarFactura(@PathVariable Integer id) {
        return compraService.generarFactura(id);
    }
}
