package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.model.Cancion;
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
    // Registrar proveedor 
    @PostMapping("/registrar")
    public Map<String, String> registrar(@RequestBody Proveedor proveedor) {
        String msg = proveedorService.registrar(proveedor);
        return Map.of("mensaje", msg);
    }

    // Registro completo
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

    // Guardar genérico 
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

    // Canciones del proveedor
    @GetMapping("/{id}/canciones")
    public List<Cancion> canciones(@PathVariable Integer id) { return proveedorService.verCanciones(id); }

    // Publicar vinilo (asocia automáticamente el proveedor)
    @PostMapping("/{id}/vinilos")
    public ResponseEntity<?> publicarVinilo(@PathVariable Integer id, @RequestBody Vinilo vinilo) {
        try {
            Vinilo creado = proveedorService.publicarVinilo(id, vinilo);
            return ResponseEntity.status(201).body(Map.of(
                    "mensaje", "Vinilo publicado",
                    "idProducto", creado.getIdProducto()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("mensaje", e.getMessage()));
        }
    }

    // Publicar canción (gratis si precio null/0)
    @PostMapping("/{id}/canciones")
    public ResponseEntity<?> publicarCancion(@PathVariable Integer id, @RequestBody Cancion cancion) {
        try {
            Cancion creada = proveedorService.publicarCancion(id, cancion);
            return ResponseEntity.status(201).body(Map.of(
                    "mensaje", creada.getPrecio() != null && creada.getPrecio() > 0 ? "Canción de pago publicada" : "Canción gratis publicada",
                    "idProducto", creada.getIdProducto(),
                    "precio", creada.getPrecio()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("mensaje", e.getMessage()));
        }
    }

    // Inventario combinado (vinilos + canciones)
    @GetMapping("/{id}/inventario")
    public ResponseEntity<?> inventario(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(proveedorService.inventarioCompleto(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("mensaje", e.getMessage()));
        }
    }

    // Actualizar vinilo del proveedor (estado, precio, etc.)
    @PutMapping("/{id}/vinilos/{idVinilo}")
    public ResponseEntity<?> actualizarVinilo(@PathVariable Integer id, @PathVariable Integer idVinilo, @RequestBody Vinilo cambios) {
        try {
            Vinilo actualizado = proveedorService.editarVinilo(id, idVinilo, cambios);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Vinilo actualizado",
                    "idProducto", actualizado.getIdProducto(),
                    "estado", actualizado.getEstado(),
                    "precio", actualizado.getPrecio(),
                    "cantidadDisponible", actualizado.getCantidadDisponible()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("mensaje", e.getMessage()));
        }
    }

    // Actualizar canción del proveedor (estado, precio, etc.)
    @PutMapping("/{id}/canciones/{idCancion}")
    public ResponseEntity<?> actualizarCancion(@PathVariable Integer id, @PathVariable Integer idCancion, @RequestBody Cancion cambios) {
        try {
            Cancion actualizada = proveedorService.editarCancion(id, idCancion, cambios);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Canción actualizada",
                    "idProducto", actualizada.getIdProducto(),
                    "estado", actualizada.getEstado(),
                    "precio", actualizada.getPrecio()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("mensaje", e.getMessage()));
        }
    }

    // Historial de pedidos 
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

