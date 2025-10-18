package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;

    private String nombre; // ADMIN, PROVEEDOR, CLIENTE
    private String descripcion;

    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;

    public Rol() {}

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}

