package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    // Usuario que realiza la compra
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Método de pago usado
    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    // Fecha y hora de la compra
    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    // Total de la compra
    @Column(name = "total")
    private Double total;

    // Productos adquiridos (Canciones y Vinilos)
    @ManyToMany
    @JoinTable(
        name = "compra_producto",
        joinColumns = @JoinColumn(name = "id_compra"),
        inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
    private List<Producto> productos;

    // Relación opcional con Pedido
    @OneToOne
    @JoinColumn(name = "id_pedido", nullable = true)
    private Pedido pedido;

    // Estado de la compra (por ejemplo: COMPLETADA, PENDIENTE, CANCELADA)
    @Column(name = "estado", length = 50)
    private String estado;

    // Constructor vacío
    public Compra() {}

    // Getters y Setters
    public Integer getIdCompra() { return idCompra; }
    public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
