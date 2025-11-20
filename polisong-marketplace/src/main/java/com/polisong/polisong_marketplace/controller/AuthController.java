package com.polisong.polisong_marketplace.controller;

import com.polisong.polisong_marketplace.model.*;
import com.polisong.polisong_marketplace.service.UsuarioService;
import com.polisong.polisong_marketplace.service.ProveedorService;
import com.polisong.polisong_marketplace.service.AdministradorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final ProveedorService proveedorService;
    private final AdministradorService administradorService;

    public AuthController(UsuarioService usuarioService,
                          ProveedorService proveedorService,
                          AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.proveedorService = proveedorService;
        this.administradorService = administradorService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        if (request == null || request.correoPrincipal == null || request.contrasena == null || request.nombreUsuario == null || request.rol == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Datos incompletos."));
        }
        // Verificar correo existente
        // Para proveedores ahora se validará contra tabla proveedor; usuarios contra usuario
        if (roleIsUser(request.rol) && usuarioService.buscarPorEmail(request.correoPrincipal) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El correo ya está registrado como usuario."));
        }

        Role role;
        try {
            role = Role.valueOf(request.rol.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Rol inválido."));
        }

        Usuario usuario = null;
        if (role != Role.PROVEEDOR) {
            usuario = new Usuario();
            usuario.setNombreUsuario(request.nombreUsuario);
            usuario.setCorreoPrincipal(request.correoPrincipal);
            usuario.setContrasena(request.contrasena); // TODO: cifrar
            usuario.setRol(role);
            usuario.setActivo(true);
        }

        switch (role) {
            case PROVEEDOR -> {
                Proveedor proveedor = proveedorService.registrarAutonomo(request.aliasContacto, request.correoPrincipal, request.contrasena);
                return ResponseEntity.ok(Map.of(
                        "mensaje", "Registro de proveedor exitoso.",
                        "idProveedor", proveedor.getIdProveedor(),
                        "correo", proveedor.getCorreo(),
                        "rol", role.name()
                ));
            }
            case ADMIN -> {
                // Crear usuario y luego administrador
                Usuario guardado = usuarioService.guardar(usuario);
                Administrador admin = new Administrador();
                admin.setAreaResponsable(request.areaResponsable != null ? request.areaResponsable : "General");
                admin.setRol(Role.ADMIN);
                administradorService.guardar(admin);
                return ResponseEntity.ok(Map.of(
                        "mensaje", "Registro de administrador exitoso.",
                        "idUsuario", guardado.getIdUsuario(),
                        "idAdmin", admin.getIdAdmin(),
                        "rol", role.name()
                ));
            }
            default -> {
                String msg = usuarioService.registrar(usuario);
                Usuario creado = usuario != null ? usuarioService.buscarPorEmail(usuario.getCorreoPrincipal()) : null;
                return ResponseEntity.ok(Map.of(
                        "mensaje", msg,
                        "idUsuario", creado != null ? creado.getIdUsuario() : null,
                        "rol", role.name()
                ));
            }
        }
    }

    // DTO interno
    public static class RegistroRequest {
        public String nombreUsuario;
        public String correoPrincipal;
        public String contrasena;
        public String rol; // USUARIO | PROVEEDOR | ADMIN
        public String aliasContacto; // proveedor
        public String contratoDisqueras; // deprecado
        public String areaResponsable; // admin
    }

    private boolean roleIsUser(String rol) {
        if (rol == null) return false;
        String r = rol.toUpperCase();
        return "USUARIO".equals(r) || "ADMIN".equals(r);
    }
}
