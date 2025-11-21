package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "vinilo")
public class Vinilo extends Producto {

    @Column(name = "artista", length = 100)
    private String artista;

    @Column(name = "año_lanzamiento")
    private Integer añoLanzamiento;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto") 
    @JsonIgnore
    private List<DetallePedido> detalles;

    public Vinilo() {}

    @Override
    public String getTipoProducto() {
        return "Vinilo";
    }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public Integer getAñoLanzamiento() { return añoLanzamiento; }
    public void setAñoLanzamiento(Integer añoLanzamiento) { this.añoLanzamiento = añoLanzamiento; }

    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
