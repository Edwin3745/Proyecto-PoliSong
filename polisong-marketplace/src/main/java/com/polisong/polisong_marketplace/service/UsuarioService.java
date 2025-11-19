package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; 

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
    public Usuario obtenerPorCorreo(String correo) {
    return usuarioRepository.findByCorreo(correo).orElse(null);
}


       // 🔹 Registrar nuevo usuario
    public String registrar(Usuario nuevo) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(nuevo.getCorreo());
        if (existente.isPresent()) {
            return "El correo ya está registrado.";
        }
        nuevo.setActivo(true);
        usuarioRepository.save(nuevo);
        return "Usuario registrado correctamente.";
    }

    // 🔹 Iniciar sesión
    public String iniciarSesion(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getContrasena().equals(contrasena)) {
                usuario.setActivo(true);
                usuarioRepository.save(usuario);
                return "Inicio de sesión exitoso.";
            } else {
                return "Contraseña incorrecta.";
            }
        }
        return "Usuario no encontrado.";
    }

    // 🔹 Cerrar sesión
    public String cerrarSesion(String correo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            return "Sesión cerrada correctamente.";
        }
        return "Usuario no encontrado.";
    }

    // 🔹 Cambiar contraseña
    public String cambiarContrasena(Integer idUsuario, String actual, String nueva) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getContrasena().equals(actual)) {
                usuario.setContrasena(nueva);
                usuarioRepository.save(usuario);
                return "Contraseña actualizada correctamente.";
            } else {
                return "La contraseña actual no coincide.";
            }
        }
        return "Usuario no encontrado.";
    }

// 🔹 Eliminar un usuario por ID
public String eliminarUsuario(Integer idUsuario) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

    if (usuarioOpt.isPresent()) {
        usuarioRepository.deleteById(idUsuario);
        return "Usuario eliminado correctamente.";
    } else {
        return "El usuario no existe.";
    }
}
}

