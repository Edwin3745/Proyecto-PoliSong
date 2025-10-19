package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "vinilo")
public class Vinilo {

    // --------------------
    // VARIABLES
    // --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vinilo")
    private Integer idVinilo;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "artista", length = 100)
    private String artista;

    @Column(name = "anio_lanzamiento")
    private Integer anioLanzamiento;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @Column(name = "estado", length = 50)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "vinilo")
    @JsonIgnore
    private List<DetallePedido> detalles;

    // --------------------
    // CONSTRUCTOR
    // --------------------
    public Vinilo() {}

    // --------------------
    // GETTERS Y SETTERS
    // --------------------
    public Integer getIdVinilo() { return idVinilo; }
    public void setIdVinilo(Integer idVinilo) { this.idVinilo = idVinilo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public Integer getAnioLanzamiento() { return anioLanzamiento; }
    public void setAnioLanzamiento(Integer anioLanzamiento) { this.anioLanzamiento = anioLanzamiento; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
