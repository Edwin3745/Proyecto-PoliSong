package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Administrador;
import com.polisong.polisong_marketplace.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<Administrador> listar() {
        return administradorRepository.findAll();
    }

    public Administrador buscarPorId(Integer id) {
        return administradorRepository.findById(id).orElse(null);
    }

    public Administrador guardar(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public void eliminar(Integer id) {
        administradorRepository.deleteById(id);
    }
}
