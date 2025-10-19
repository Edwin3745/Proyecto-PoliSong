package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "catalogo")
public class Catalogo {

    // --------------------
    // VARIABLES
    // --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo")
    private Integer idCatalogo;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany
    @JoinColumn(name = "id_catalogo")
    @JsonIgnore
    private List<Vinilo> vinilos;

    // --------------------
    // CONSTRUCTOR
    // --------------------
    public Catalogo() {}

    // --------------------
    // GETTERS Y SETTERS
    // --------------------
    public Integer getIdCatalogo() { return idCatalogo; }
    public void setIdCatalogo(Integer idCatalogo) { this.idCatalogo = idCatalogo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Vinilo> getVinilos() { return vinilos; }
    public void setVinilos(List<Vinilo> vinilos) { this.vinilos = vinilos; }
}

