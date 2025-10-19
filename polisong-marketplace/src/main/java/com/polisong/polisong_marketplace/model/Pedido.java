package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pedido")
public class Pedido {

    
    // VARIABLES
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DetallePedido> detalles;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Valoracion> valoraciones;

    // CONSTRUCTOR
 
    public Pedido() {}

   
    // GETTERS Y SETTERS
   
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    public List<Valoracion> getValoraciones() { return valoraciones; }
    public void setValoraciones(List<Valoracion> valoraciones) { this.valoraciones = valoraciones; }
}
