package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Validación (se deja preparado, comentar si no está disponible Jakarta Validation)
// import org.springframework.validation.annotation.Validated;
// import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Map<String,Object>> registrar(@RequestBody Usuario usuario) {
        String resultado = usuarioService.registrar(usuario);
        boolean exito = resultado.toLowerCase().contains("exitoso");
        if (exito) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", resultado,
                    "correo", usuario.getCorreoPrincipal(),
                    "rol", usuario.getRol() != null ? usuario.getRol().name() : "USUARIO"
            ));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", resultado));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> iniciarSesion(@RequestParam String correo, @RequestParam String contrasena) {
        String resultado = usuarioService.iniciarSesion(correo, contrasena);
        if (resultado.equals("Inicio de sesión exitoso.")) {
            // Recuperación básica del usuario (mantiene enfoque original sin nuevos métodos del service)
            Usuario usuarioMatch = usuarioService.listar().stream()
                    .filter(u -> correo.equals(u.getCorreoPrincipal()))
                    .findFirst().orElse(null);
            return ResponseEntity.ok(Map.of(
                    "mensaje", resultado,
                    "nombre", usuarioMatch != null ? usuarioMatch.getNombreUsuario() : correo,
                    "correo", correo,
                    "rol", usuarioMatch != null && usuarioMatch.getRol() != null ? usuarioMatch.getRol().name() : "USUARIO"
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", resultado));
    }

    @PostMapping("/logout")
    public String cerrarSesion(@RequestParam String correo) {
        return usuarioService.cerrarSesion(correo);
    }

    @PutMapping("/{id}/cambiarContrasena")
    public String cambiarContrasena(
            @PathVariable Integer id,
            @RequestParam String contrasenaActual,
            @RequestParam String nuevaContrasena) {
        return usuarioService.cambiarContrasena(id, contrasenaActual, nuevaContrasena);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
    }

    // =============================
    // Endpoints adicionales
    // =============================

    @PutMapping("/{id}/editarPerfil")
    public ResponseEntity<Map<String, String>> editarPerfil(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> payload) {
        String nombreUsuario = payload.get("nombreUsuario") != null ? payload.get("nombreUsuario").toString() : null;
        String correoPrincipal = payload.get("correoPrincipal") != null ? payload.get("correoPrincipal").toString() : null;
        // Mantener lógica original: delegar validación/existencia al service
        String resultado = usuarioService.editarPerfil(id, nombreUsuario, correoPrincipal);
        return ResponseEntity.ok(Map.of("mensaje", resultado));
    }

    @GetMapping("/email")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam("correo") String correo) {
        // Reutiliza enfoque actual (si no existe método dedicado en service se puede ajustar después)
        Usuario u = usuarioService.listar().stream()
                .filter(x -> correo.equals(x.getCorreoPrincipal()))
                .findFirst().orElse(null);
        if (u == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(u);
    }

    // Historial de pedidos (requiere método en service: verHistorialPedidos). Se deja temporalmente deshabilitado si no existe.
    // @GetMapping("/{id}/historial-pedidos")
    // public List<Pedido> historialPedidos(@PathVariable Integer id) {
    //     return usuarioService.verHistorialPedidos(id);
    // }

    // Endpoint de validación (requiere método validarCredenciales en service). Comentado si aún no implementado.
    // @PostMapping("/validar")
    // public Map<String, Object> validar(@RequestParam("correo") String correo,
    //                                    @RequestParam("contrasena") String contrasena) {
    //     boolean valido = usuarioService.validarCredenciales(correo, contrasena);
    //     return Map.of("valido", valido);
    // }

    // Recuperación de contraseña (requiere método enviarCorreoRecuperacion en service). Comentado si no existe.
    // @PostMapping("/recuperar-contrasena")
    // public Map<String, String> recuperarContrasena(@RequestParam("correo") String correo) {
    //     String resultado = usuarioService.enviarCorreoRecuperacion(correo);
    //     return Map.of("mensaje", resultado);
    // }
}

