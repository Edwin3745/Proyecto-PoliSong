package com.polisong.polisong_marketplace.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "proveedor")
public class Proveedor {

    // VARIABLES
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "alias_contacto", length = 100)
    private String aliasContacto;

    @Column(name = "contrato_disqueras", length = 255)
    private String contratoDisqueras;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vinilo> vinilos;

    // CONSTRUCTOR
  
    public Proveedor() {}

   
    // GETTERS Y SETTERS
   
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getAliasContacto() { return aliasContacto; }
    public void setAliasContacto(String aliasContacto) { this.aliasContacto = aliasContacto; }

    public String getContratoDisqueras() { return contratoDisqueras; }
    public void setContratoDisqueras(String contratoDisqueras) { this.contratoDisqueras = contratoDisqueras; }

    public List<Vinilo> getVinilos() { return vinilos; }
    public void setVinilos(List<Vinilo> vinilos) { this.vinilos = vinilos; }
}

