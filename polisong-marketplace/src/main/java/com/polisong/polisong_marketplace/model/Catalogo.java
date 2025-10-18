package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCatalogo;

    private String nombre;
    private String descripcion;

    @OneToMany
    @JoinColumn(name = "id_catalogo")
    private List<Producto> productos;

    public Catalogo() {}

    public int getIdCatalogo() { return idCatalogo; }
    public void setIdCatalogo(int idCatalogo) { this.idCatalogo = idCatalogo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}

