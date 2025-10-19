package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {

    // --------------------
    // VARIABLES
    // --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Integer idCarrito;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "carrito_vinilo",
        joinColumns = @JoinColumn(name = "id_carrito"),
        inverseJoinColumns = @JoinColumn(name = "id_vinilo")
    )
    private List<Vinilo> vinilos;

    @ManyToMany
    @JoinTable(
        name = "carrito_cancion",
        joinColumns = @JoinColumn(name = "id_carrito"),
        inverseJoinColumns = @JoinColumn(name = "id_cancion")
    )
    private List<Cancion> canciones;

    // --------------------
    // CONSTRUCTOR
    // --------------------
    public Carrito() {}

    // --------------------
    // GETTERS Y SETTERS
    // --------------------
    public Integer getIdCarrito() { return idCarrito; }
    public void setIdCarrito(Integer idCarrito) { this.idCarrito = idCarrito; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Vinilo> getVinilos() { return vinilos; }
    public void setVinilos(List<Vinilo> vinilos) { this.vinilos = vinilos; }

    public List<Cancion> getCanciones() { return canciones; }
    public void setCanciones(List<Cancion> canciones) { this.canciones = canciones; }
}


