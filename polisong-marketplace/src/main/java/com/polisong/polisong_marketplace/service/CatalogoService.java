package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Catalogo;
import com.polisong.polisong_marketplace.repository.CatalogoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;

    public CatalogoService(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    public List<Catalogo> listar() {
        return catalogoRepository.findAll();
    }

    public Catalogo buscarPorId(Integer id) {
        return catalogoRepository.findById(id).orElse(null);
    }

    public Catalogo guardar(Catalogo catalogo) {
        return catalogoRepository.save(catalogo);
    }

    public void eliminar(Integer id) {
        catalogoRepository.deleteById(id);
    }
}
