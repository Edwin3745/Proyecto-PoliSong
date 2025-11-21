package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cancion")
public class Cancion extends Producto {

    @Column(name = "duracion")
    private Double duracion;

    @Column(name = "tamaño") 
    private Double tamanio;

    @Column(name = "calidad_kbps")
    private Integer calidadKbps;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    public Cancion() {}

    @Override
    public String getTipoProducto() {
        return "Canción";
    }

  
    public Double getDuracion() { return duracion; }
    public void setDuracion(Double duracion) { this.duracion = duracion; }

    public Double getTamanio() { return tamanio; }
    public void setTamanio(Double tamanio) { this.tamanio = tamanio; }

    public Integer getCalidadKbps() { return calidadKbps; }
    public void setCalidadKbps(Integer calidadKbps) { this.calidadKbps = calidadKbps; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
}

