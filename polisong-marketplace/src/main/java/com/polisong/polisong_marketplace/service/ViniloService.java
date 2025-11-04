package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.repository.ViniloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViniloService {

    @Autowired
    private ViniloRepository viniloRepository;

    // -----------------------------------
    // Registrar Vinilo
    // -----------------------------------
    public Vinilo registrarVinilo(Vinilo vinilo) {
        if (vinilo == null) {
            throw new IllegalArgumentException("El vinilo no puede ser nulo");
        }
        return viniloRepository.save(vinilo);
    }

    // -----------------------------------
    // Actualizar Vinilo
    // -----------------------------------
    public Vinilo actualizarVinilo(Integer idVinilo, Vinilo viniloActualizado) {
        Optional<Vinilo> viniloOpt = viniloRepository.findById(idVinilo);

        if (viniloOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el vinilo con ID: " + idVinilo);
        }

        Vinilo vinilo = viniloOpt.get();
        vinilo.setNombre(viniloActualizado.getNombre());
        vinilo.setArtista(viniloActualizado.getArtista());
        vinilo.setAnioLanzamiento(viniloActualizado.getAnioLanzamiento());
        vinilo.setPrecio(viniloActualizado.getPrecio());
        vinilo.setCantidadDisponible(viniloActualizado.getCantidadDisponible());
        vinilo.setEstado(viniloActualizado.getEstado());
        vinilo.setProveedor(viniloActualizado.getProveedor());

        return viniloRepository.save(vinilo);
    }

    // -----------------------------------
    // Consultar Vinilo por ID
    // -----------------------------------
    public Vinilo consultarVinilo(Integer idVinilo) {
        Optional<Vinilo> viniloOpt = viniloRepository.findById(idVinilo);

        if (viniloOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el vinilo con ID: " + idVinilo);
        }

        return viniloOpt.get();
    }

    // -----------------------------------
    // Listar todos los Vinilos
    // -----------------------------------
    public List<Vinilo> listarVinilos() {
        return viniloRepository.findAll();
    }
}


