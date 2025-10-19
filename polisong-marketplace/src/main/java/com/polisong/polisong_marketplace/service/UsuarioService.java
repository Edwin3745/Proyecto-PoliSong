package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
