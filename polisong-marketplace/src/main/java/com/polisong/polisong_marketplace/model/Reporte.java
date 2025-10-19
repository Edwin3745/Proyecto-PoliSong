package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reporte")
public class Reporte {

    // --------------------
    // VARIABLES
    // --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(name = "tipo", length = 50)
    private String tipo; // "VENTAS", "USUARIOS", etc.

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "total_generado")
    private Double totalGenerado;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Usuario administrador;

    // --------------------
    // CONSTRUCTOR
    // --------------------
    public Reporte() {}

    // --------------------
    // GETTERS Y SETTERS
    // --------------------
    public Integer getIdReporte() { return idReporte; }
    public void setIdReporte(Integer idReporte) { this.idReporte = idReporte; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getTotalGenerado() { return totalGenerado; }
    public void setTotalGenerado(Double totalGenerado) { this.totalGenerado = totalGenerado; }

    public Usuario getAdministrador() { return administrador; }
    public void setAdministrador(Usuario administrador) { this.administrador = administrador; }
}

