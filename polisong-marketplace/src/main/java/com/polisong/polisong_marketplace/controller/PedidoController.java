package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.service.PedidoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public Pedido obtenerPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id);
    }

    @PostMapping
    public Pedido guardar(@RequestBody Pedido pedido) {
        return pedidoService.guardar(pedido);
    }

    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable Integer id, @RequestBody Pedido pedido) {
        Pedido existente = pedidoService.buscarPorId(id);
        if (existente != null) {
            pedido.setIdPedido(id);
            return pedidoService.guardar(pedido);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        pedidoService.eliminar(id);
    }
}
