package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @GetMapping("/{cedula}")
    public Optional<Usuario> buscarPorCedula(@PathVariable String cedula) {
        return usuarioService.buscarPorCedula(cedula);
    }
}
