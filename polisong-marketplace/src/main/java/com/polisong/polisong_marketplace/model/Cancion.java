package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cancion")
public class Cancion {

    
    // VARIABLES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancion")
    private Integer idCancion;

    @Column(name = "nombre_cancion", length = 100)
    private String nombreCancion;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "duracion")
    private Double duracion;

    @Column(name = "tamanio")
    private Double tamanio;

    @Column(name = "calidad_kbps")
    private Integer calidadKbps;

    @OneToMany(mappedBy = "cancion")
    @JsonIgnore
    private List<DetallePedido> detalles;

   
    // CONSTRUCTOR
   
    public Cancion() {}

    // GETTERS Y SETTERS
   
    public Integer getIdCancion() { return idCancion; }
    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }

    public String getNombreCancion() { return nombreCancion; }
    public void setNombreCancion(String nombreCancion) { this.nombreCancion = nombreCancion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Double getDuracion() { return duracion; }
    public void setDuracion(Double duracion) { this.duracion = duracion; }

    public Double getTamanio() { return tamanio; }
    public void setTamanio(Double tamanio) { this.tamanio = tamanio; }

    public Integer getCalidadKbps() { return calidadKbps; }
    public void setCalidadKbps(Integer calidadKbps) { this.calidadKbps = calidadKbps; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
