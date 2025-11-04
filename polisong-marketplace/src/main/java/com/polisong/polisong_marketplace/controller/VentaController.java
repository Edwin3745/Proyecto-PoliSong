package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Venta;
import com.polisong.polisong_marketplace.service.VentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public Venta buscarPorId(@PathVariable Integer id) {
        return ventaService.buscarPorId(id);
    }

    @PostMapping("/registrar")
    public Venta registrarVenta(@RequestBody Venta venta) {
        return ventaService.registrarVenta(venta);
    }

    @GetMapping("/consultar")
    public List<Venta> consultarVentas() {
        return ventaService.consultarVentas();
    }

    @PutMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        return ventaService.actualizarEstadoVenta(id, nuevoEstado);
    }

    @PutMapping("/{id}/proveedor")
    public String actualizarProveedor(@PathVariable Integer id, @RequestParam Integer idProveedor) {
        return ventaService.actualizarVentaProveedor(id, idProveedor);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
    }
}
