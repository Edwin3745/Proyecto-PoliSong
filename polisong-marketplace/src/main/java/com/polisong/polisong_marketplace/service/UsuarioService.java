package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Role;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.repository.PedidoRepository;
import com.polisong.polisong_marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
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

    // =============================
    // Nuevos métodos solicitados
    // =============================

    // Editar perfil (actualiza campos básicos; no cambia contraseña)
    public String editarPerfil(Integer idUsuario, String nombreUsuario, String correoPrincipal) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            return "Usuario no encontrado.";
        }
        Usuario u = usuarioOpt.get();
        if (correoPrincipal != null && !correoPrincipal.equals(u.getCorreoPrincipal())) {
            if (usuarioRepository.findByCorreoPrincipal(correoPrincipal).isPresent()) {
                return "El correo ya está registrado por otro usuario.";
            }
            u.setCorreoPrincipal(correoPrincipal);
        }
        if (nombreUsuario != null) u.setNombreUsuario(nombreUsuario);
        usuarioRepository.save(u);
        return "Perfil actualizado correctamente.";
    }

    // Buscar por email (correoPrincipal)
    public Usuario buscarPorEmail(String correoPrincipal) {
        return usuarioRepository.findByCorreoPrincipal(correoPrincipal).orElse(null);
    }

    // Ver historial de pedidos del usuario
    public List<Pedido> verHistorialPedidos(Integer idUsuario) {
        // Nota: si el repositorio tiene un método derivado incompatible con el modelo,
        // usamos un filtrado seguro por ahora.
        try {
            // Intentar usar método derivado existente si aplica
            return pedidoRepository.findByUsuario_IdUsuario(idUsuario);
        } catch (Exception ignored) {
            // Fallback: filtrar en memoria por id (usando Usuario.idUsuario)
            List<Pedido> all = pedidoRepository.findAll();
            List<Pedido> result = new ArrayList<>();
            for (Pedido p : all) {
                if (p.getUsuario() != null && p.getUsuario().getIdUsuario() != null
                        && p.getUsuario().getIdUsuario().equals(idUsuario)) {
                    result.add(p);
                }
            }
            return result;
        }
    }

    // Validar credenciales sin iniciar sesión
    public boolean validarCredenciales(String correoPrincipal, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoPrincipal(correoPrincipal);
        return usuarioOpt.isPresent() && contrasena != null && contrasena.equals(usuarioOpt.get().getContrasena());
    }

    // Generar y enviar correo de recuperación de contraseña (temporal)
    public String enviarCorreoRecuperacion(String correoPrincipal) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoPrincipal(correoPrincipal);
        if (usuarioOpt.isEmpty()) {
            return "No existe un usuario con ese correo.";
        }
        Usuario u = usuarioOpt.get();
        String temp = generarContrasenaTemporal(8);
        u.setContrasena(temp);
        usuarioRepository.save(u);
        notificacionService.enviarCorreoNotificacion(correoPrincipal,
                "Recuperación de contraseña",
                "Tu contraseña temporal es: " + temp + "\nPor favor, cámbiala al iniciar sesión.");
        return "Se envió un correo con la contraseña temporal.";
    }

    private String generarContrasenaTemporal(int length) {
        final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789"; // sin confusos
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}

