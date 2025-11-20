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
        if (request == null || request.rol == null || request.contrasena == null || request.correoPrincipal == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Faltan campos obligatorios."));
        }

        Role role;
        try {
            role = Role.valueOf(request.rol.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Rol inválido."));
        }

        // Validación específica por rol
        if (role == Role.USUARIO || role == Role.ADMIN) {
            if (request.nombreUsuario == null || request.nombreUsuario.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Nombre de usuario requerido."));
            }
        } else if (role == Role.PROVEEDOR) {
            if (request.aliasContacto == null || request.aliasContacto.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Alias de contacto requerido para proveedor."));
            }
        }

        // Verificar correo existente sólo para usuarios (proveedor se valida aparte en la capa de servicio si se implementa)
        if (role != Role.PROVEEDOR) {
            boolean correoExiste = usuarioService.listar().stream()
                    .anyMatch(u -> request.correoPrincipal.equalsIgnoreCase(u.getCorreoPrincipal()));
            if (correoExiste) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El correo ya está registrado como usuario."));
            }
        }

        Usuario usuario = null;
        if (role != Role.PROVEEDOR) {
            usuario = new Usuario();
            usuario.setNombreUsuario(request.nombreUsuario);
            usuario.setCorreoPrincipal(request.correoPrincipal);
            usuario.setContrasena(request.contrasena); // TODO: aplicar hash BCrypt
            usuario.setRol(role);
            usuario.setActivo(true);
        }

        switch (role) {
            case PROVEEDOR -> {
                Proveedor proveedor = proveedorService.registrarAutonomo(request.aliasContacto, request.correoPrincipal, request.contrasena);
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
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
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "mensaje", "Registro de administrador exitoso.",
                        "idUsuario", guardado.getIdUsuario(),
                        "idAdmin", admin.getIdAdmin(),
                        "rol", role.name()
                ));
            }
            default -> {
                String msg = usuarioService.registrar(usuario);
                // El servicio llena el ID en la misma instancia 'usuario' tras persistir
                Usuario creado = usuario;
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
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

    // helper legacy (no usado tras refactor); conservar comentado para referencia
    // private boolean roleIsUser(String rol) {
    //     if (rol == null) return false;
    //     String r = rol.toUpperCase();
    //     return "USUARIO".equals(r) || "ADMIN".equals(r);
    // }
}
