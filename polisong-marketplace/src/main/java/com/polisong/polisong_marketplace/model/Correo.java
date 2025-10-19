package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "correo")
public class Correo {

    // --------------------
    // VARIABLES
    // --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correo")
    private Integer idCorreo;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // --------------------
    // CONSTRUCTOR
    // --------------------
    public Correo() {}

    // --------------------
    // GETTERS Y SETTERS
    // --------------------
    public Integer getIdCorreo() { return idCorreo; }
    public void setIdCorreo(Integer idCorreo) { this.idCorreo = idCorreo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
