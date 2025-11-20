package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrar(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestParam String correo, @RequestParam String contrasena) {
        String resultado = usuarioService.iniciarSesion(correo, contrasena);
        if (resultado.equals("Inicio de sesión exitoso.")) {
            Usuario usuario = usuarioService.listar()
                    .stream().filter(u -> u.getCorreoPrincipal().equals(correo)).findFirst().orElse(null);
            return ResponseEntity.ok(Map.of(
                "mensaje", resultado,
                "nombre", usuario != null ? usuario.getNombreUsuario() : correo,
                "correo", correo,
                "rol", usuario != null && usuario.getRol() != null ? usuario.getRol().name() : "USUARIO"
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("mensaje", resultado));
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
        String resultado = usuarioService.editarPerfil(id, nombreUsuario, correoPrincipal);
        return ResponseEntity.ok(Map.of("mensaje", resultado));
    }

    @GetMapping("/email")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam("correo") String correo) {
        Usuario u = usuarioService.buscarPorEmail(correo);
        if (u == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(u);
    }

    @GetMapping("/{id}/historial-pedidos")
    public List<Pedido> historialPedidos(@PathVariable Integer id) {
        return usuarioService.verHistorialPedidos(id);
    }

    @PostMapping("/validar")
    public Map<String, Object> validar(@RequestParam("correo") String correo,
                                       @RequestParam("contrasena") String contrasena) {
        boolean valido = usuarioService.validarCredenciales(correo, contrasena);
        return Map.of("valido", valido);
    }

    @PostMapping("/recuperar-contrasena")
    public Map<String, String> recuperarContrasena(@RequestParam("correo") String correo) {
        String resultado = usuarioService.enviarCorreoRecuperacion(correo);
        return Map.of("mensaje", resultado);
    }
}

