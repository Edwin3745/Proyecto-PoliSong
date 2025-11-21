package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import com.polisong.polisong_marketplace.model.Role;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre_usuario", length = 100, nullable = false)
    private String nombreUsuario;

    @Column(name = "correo_principal", length = 100, nullable = false, unique = true)
    private String correoPrincipal;

    @Column(name = "contrasena", length = 255)
    private String contrasena;


    @Column(name = "activo")
    private Boolean activo;


    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    private Role rol = Role.USUARIO;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Compra> compras;

    public Usuario() {}

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getCorreoPrincipal() { return correoPrincipal; }
    public void setCorreoPrincipal(String correoPrincipal) { this.correoPrincipal = correoPrincipal; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }


    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }
}
