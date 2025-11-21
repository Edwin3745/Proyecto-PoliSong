package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import com.polisong.polisong_marketplace.model.Role;

@Entity
@Table(name = "telefono")
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Integer idTelefono;

    @Column(name = "numero", length = 20)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20)
    private Role rol; 

    public Telefono() {}

    public Integer getIdTelefono() { return idTelefono; }
    public void setIdTelefono(Integer idTelefono) { this.idTelefono = idTelefono; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Role getRol() { return rol; }
    public void setRol(Role rol) { this.rol = rol; }
}
