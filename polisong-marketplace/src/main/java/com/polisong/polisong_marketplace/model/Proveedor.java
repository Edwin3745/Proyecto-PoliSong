package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import com.polisong.polisong_marketplace.model.Role;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "alias_contacto", length = 100)
    private String aliasContacto;

    
    @Column(name = "correo", length = 120, unique = true, nullable = false)
    private String correo;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    private Role rol = Role.PROVEEDOR;



    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vinilo> vinilos;

    public Proveedor() {}

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public String getAliasContacto() { return aliasContacto; }
    public void setAliasContacto(String aliasContacto) { this.aliasContacto = aliasContacto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }


    public List<Vinilo> getVinilos() { return vinilos; }
    public void setVinilos(List<Vinilo> vinilos) { this.vinilos = vinilos; }

    public String getNombreProveedor() {
        return this.aliasContacto != null ? this.aliasContacto : "Proveedor sin alias";
    }
}

