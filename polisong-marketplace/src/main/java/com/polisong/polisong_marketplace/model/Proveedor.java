package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;

@Entity
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProveedor;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String aliasContacto;
    private String contratoDisqueras;

    public Proveedor() {}

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getAliasContacto() { return aliasContacto; }
    public void setAliasContacto(String aliasContacto) { this.aliasContacto = aliasContacto; }

    public String getContratoDisqueras() { return contratoDisqueras; }
    public void setContratoDisqueras(String contratoDisqueras) { this.contratoDisqueras = contratoDisqueras; }
}

