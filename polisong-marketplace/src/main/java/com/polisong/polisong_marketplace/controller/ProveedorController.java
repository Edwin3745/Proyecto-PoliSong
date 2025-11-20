package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.model.Proveedor;
import com.polisong.polisong_marketplace.service.ProveedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<Proveedor> listar() { return proveedorService.listar(); }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Integer id) {
        Proveedor p = proveedorService.buscarPorId(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }
    // Registrar proveedor (semántico)
    @PostMapping("/registrar")
    public Map<String, String> registrar(@RequestBody Proveedor proveedor) {
        String msg = proveedorService.registrar(proveedor);
        return Map.of("mensaje", msg);
    }

    // Registro completo: usuario + proveedor
    @PostMapping("/registrar-completo")
    public ResponseEntity<?> registrarCompleto(@RequestBody RegistroProveedorRequest request) {
        if (request == null || request.correo == null || request.contrasena == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Datos incompletos para registro de proveedor."));
        }
        Proveedor proveedor = proveedorService.registrarAutonomo(
                request.aliasContacto,
                request.correo,
                request.contrasena
        );
        return ResponseEntity.ok(Map.of(
                "mensaje", "Proveedor registrado correctamente.",
                "idProveedor", proveedor.getIdProveedor(),
                "correo", proveedor.getCorreo(),
                "rol", proveedor.getRol().name()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginProveedor(@RequestParam String correo, @RequestParam String contrasena) {
        Proveedor p = proveedorService.login(correo, contrasena);
        if (p == null) {
            return ResponseEntity.status(401).body(Map.of("mensaje", "Credenciales inválidas."));
        }
        return ResponseEntity.ok(Map.of(
                "mensaje", "Login exitoso.",
                "rol", p.getRol().name(),
                "alias", p.getAliasContacto(),
                "idProveedor", p.getIdProveedor()
        ));
    }

    // Guardar genérico (retrocompatibilidad)
    @PostMapping
    public Proveedor guardar(@RequestBody Proveedor proveedor) { return proveedorService.guardar(proveedor); }

    @PutMapping("/{id}")
    public Map<String,String> actualizar(@PathVariable Integer id, @RequestBody Proveedor proveedor) {
        String msg = proveedorService.actualizar(id, proveedor);
        return Map.of("mensaje", msg);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) { proveedorService.eliminar(id); }

    // Productos (vinilos) del proveedor
    @GetMapping("/{id}/productos")
    public List<Vinilo> productos(@PathVariable Integer id) { return proveedorService.verProductos(id); }

    // Historial de pedidos (derivado de ventas con pedidos adjuntos)
    @GetMapping("/{id}/historial-pedidos")
    public List<Pedido> historialPedidos(@PathVariable Integer id) { return proveedorService.verHistorialPedidos(id); }

    // Estadísticas de venta
    @GetMapping("/{id}/estadisticas-venta")
    public Map<String,Object> estadisticas(@PathVariable Integer id) { return proveedorService.estadisticasVenta(id); }
}

// DTO para registro completo
class RegistroProveedorRequest {
    public String aliasContacto;
    public String correo;
    public String contrasena;
}

