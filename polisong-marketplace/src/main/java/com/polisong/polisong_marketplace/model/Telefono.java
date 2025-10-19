package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "telefono")
public class Telefono {

    // --------------------
    // VARIABLES
    // --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Integer idTelefono;

    @Column(name = "numero", length = 20)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // --------------------
    // CONSTRUCTOR
    // --------------------
    public Telefono() {}

    // --------------------
    // GETTERS Y SETTERS
    // --------------------
    public Integer getIdTelefono() { return idTelefono; }
    public void setIdTelefono(Integer idTelefono) { this.idTelefono = idTelefono; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
