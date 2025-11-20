package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Herencia real en la BD
@Table(name = "producto")
public abstract class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "estado", length = 50)
    private String estado;

    // Método abstracto: define el tipo de producto (Canción o Vinilo)
    public abstract String getTipoProducto();

    // CONSTRUCTOR
    public Producto() {}

    // GETTERS Y SETTERS
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
