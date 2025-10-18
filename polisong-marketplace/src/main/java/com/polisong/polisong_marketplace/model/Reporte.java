package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReporte;

    private String tipo; // "VENTAS" o "USUARIOS"
    private Date fechaGeneracion;
    private String descripcion;
    private double totalGenerado;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Usuario administrador;

    public Reporte() {}

    public int getIdReporte() { return idReporte; }
    public void setIdReporte(int idReporte) { this.idReporte = idReporte; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Date getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Date fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getTotalGenerado() { return totalGenerado; }
    public void setTotalGenerado(double totalGenerado) { this.totalGenerado = totalGenerado; }

    public Usuario getAdministrador() { return administrador; }
    public void setAdministrador(Usuario administrador) { this.administrador = administrador; }
}
