package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import com.polisong.polisong_marketplace.model.Role;

@Entity
@Table(name = "correo")
public class Correo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo")
    private Integer idCorreo;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20)
    private Role rol; // opcional: puede permanecer null

    public Correo() {}

    public Integer getIdCorreo() { return idCorreo; }
    public void setIdCorreo(Integer idCorreo) { this.idCorreo = idCorreo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }
}
