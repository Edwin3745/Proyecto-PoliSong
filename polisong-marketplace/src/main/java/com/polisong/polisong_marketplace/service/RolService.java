package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Rol;
import com.polisong.polisong_marketplace.model.Usuario;
import com.polisong.polisong_marketplace.repository.RolRepository;
import com.polisong.polisong_marketplace.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listar() {
        return rolRepository.findAll();
    }

    public Rol buscarPorId(Integer id) {
        return rolRepository.findById(id).orElse(null);
    }

    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    public void eliminar(Integer id) {
        rolRepository.deleteById(id);
    }
    //Asignar un rol a un usuario
    public String asignarRolAUsuario(Integer idUsuario, Integer idRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        Optional<Rol> rolOpt = rolRepository.findById(idRol);

        if (usuarioOpt.isPresent() && rolOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Rol rol = rolOpt.get();

            usuario.getRoles().add(rol);
            usuarioRepository.save(usuario);
            return "Rol '" + rol.getNombre() + "' asignado al usuario '" + usuario.getNombre() + "'";
        } else {
            return "Usuario o Rol no encontrado";
        }
    }
     //Eliminar un rol asignado a un usuario
     
    public String eliminarRolDeUsuario(Integer idUsuario, Integer idRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        Optional<Rol> rolOpt = rolRepository.findById(idRol);

        if (usuarioOpt.isPresent() && rolOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Rol rol = rolOpt.get();

            if (usuario.getRoles().remove(rol)) {
                usuarioRepository.save(usuario);
                return "Rol '" + rol.getNombre() + "' eliminado del usuario '" + usuario.getNombre() + "'";
            } else {
                return "El usuario no tiene asignado ese rol";
            }
        } else {
            return "Usuario o Rol no encontrado";
        }
    }

    // Listar los roles asignados a un usuario específico
    public List<Rol> listarRolesPorUsuario(Integer idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        return usuarioOpt.map(Usuario::getRoles).orElse(List.of());
    }

    // Verificar si un usuario tiene un rol determinado
    public boolean usuarioTieneRol(Integer idUsuario, String nombreRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return usuario.getRoles().stream()
                    .anyMatch(r -> r.getNombre().equalsIgnoreCase(nombreRol));
        }
        return false;
    }
}