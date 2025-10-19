package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detallepedido")
public class DetallePedido {

    // VARIABLES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_vinilo")
    private Vinilo vinilo;

    @ManyToOne
    @JoinColumn(name = "id_cancion")
    private Cancion cancion;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @Column(name = "subtotal")
    private Double subtotal;


    // CONSTRUCTOR

    public DetallePedido() {}

    // GETTERS Y SETTERS

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Vinilo getVinilo() { return vinilo; }
    public void setVinilo(Vinilo vinilo) { this.vinilo = vinilo; }

    public Cancion getCancion() { return cancion; }
    public void setCancion(Cancion cancion) { this.cancion = cancion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}
