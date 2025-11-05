package com.polisong.polisong_marketplace.controller;

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

    //Listar todos los usuarios
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    //Buscar por ID
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }

    //Registrar nuevo usuario
    @PostMapping("/registrar")
    public String registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrar(usuario);
    }

    //Iniciar sesión
    @PostMapping("/login")
public ResponseEntity<?> iniciarSesion(@RequestParam String correo, @RequestParam String contrasena) {
    Usuario usuario = usuarioService.obtenerPorCorreo(correo);
    String resultado = usuarioService.iniciarSesion(correo, contrasena);

    if (resultado.equals("Inicio de sesión exitoso.")) {
        return ResponseEntity.ok(Map.of(
            "mensaje", resultado,
            "nombre", usuario.getNombre(),
            "correo", usuario.getCorreo()
        ));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("mensaje", resultado));
}


    // Cerrar sesión
    @PostMapping("/logout")
    public String cerrarSesion(@RequestParam String correo) {
        return usuarioService.cerrarSesion(correo);
    }

    //Cambiar contraseña
    @PutMapping("/{id}/cambiarContrasena")
    public String cambiarContrasena(
            @PathVariable Integer id,
            @RequestParam String contrasenaActual,
            @RequestParam String nuevaContrasena) {
        return usuarioService.cambiarContrasena(id, contrasenaActual, nuevaContrasena);
    }

    // Eliminar usuario 
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
    }
    
}
