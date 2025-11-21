package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Role;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.repository.PedidoRepository;
import com.polisong.polisong_marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    

    private final PedidoRepository pedidoRepository;
    
    private final NotificacionService notificacionService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository,
                          NotificacionService notificacionService) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.notificacionService = notificacionService;
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

    public String registrar(Usuario nuevo) {
        Optional<Usuario> existente = usuarioRepository.findByCorreoPrincipal(nuevo.getCorreoPrincipal());
        if (existente.isPresent()) {
            return "El correo ya está registrado.";
        }
        nuevo.setActivo(true);
        if (nuevo.getRol() == null) {
            nuevo.setRol(Role.USUARIO);
        }
        usuarioRepository.save(nuevo);
        return "Usuario registrado correctamente.";
    }

    public String iniciarSesion(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoPrincipal(correo);
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

    public String cerrarSesion(String correo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoPrincipal(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            return "Sesión cerrada correctamente.";
        }
        return "Usuario no encontrado.";
    }

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

    // Actualiza nombre y/o correo si se proporcionan. Valida duplicado de correo.
    public String editarPerfil(Integer id, String nombreUsuario, String correoPrincipal) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if (opt.isEmpty()) return "Usuario no encontrado.";
        Usuario usuario = opt.get();
        boolean modificado = false;
        if (correoPrincipal != null && !correoPrincipal.isBlank()) {
            Optional<Usuario> existeCorreo = usuarioRepository.findByCorreoPrincipal(correoPrincipal);
            if (existeCorreo.isPresent() && !existeCorreo.get().getIdUsuario().equals(id)) {
                return "El correo ya está registrado.";
            }
            usuario.setCorreoPrincipal(correoPrincipal);
            modificado = true;
        }
        if (nombreUsuario != null && !nombreUsuario.isBlank()) {
            usuario.setNombreUsuario(nombreUsuario);
            modificado = true;
        }
        if (!modificado) {
            return "No se enviaron campos para actualizar.";
        }
        usuarioRepository.save(usuario);
        return "Perfil actualizado correctamente.";
    }
}
