package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {

    private final Map<String, Usuario> store = new ConcurrentHashMap<>();

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(store.values());
    }

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        String cedula = usuario.getCedula();
        if (cedula == null || cedula.isEmpty()) {
            // generate a simple id if cedula not provided
            cedula = UUID.randomUUID().toString();
            usuario.setCedula(cedula);
        }
        store.put(cedula, usuario);
        return usuario;
    }

    public Optional<Usuario> buscarPorCedula(String cedula) {
        return Optional.ofNullable(store.get(cedula));
    }
}