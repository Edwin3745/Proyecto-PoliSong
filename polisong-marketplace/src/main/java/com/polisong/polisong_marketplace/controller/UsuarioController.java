package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario existente = usuarioService.buscarPorId(id);
        if (existente != null) {
            usuario.setIdUsuario(id);
            return usuarioService.guardar(usuario);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
    }
}
