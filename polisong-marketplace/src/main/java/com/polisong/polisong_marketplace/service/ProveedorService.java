package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Proveedor;
import com.polisong.polisong_marketplace.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }
// 1️ Registrar proveedor con estado "Activo"
public Proveedor registrarProveedor(Proveedor proveedor) {
    proveedor.setEstado("Activo"); 
    return proveedorRepository.save(proveedor);
}

// 2️ Actualizar datos del proveedor
public Proveedor actualizarProveedor(Integer id, Proveedor nuevosDatos) {
    Proveedor proveedor = proveedorRepository.findById(id).orElse(null);
    if (proveedor != null) {
        proveedor.setNombre(nuevosDatos.getNombre());
        proveedor.setTelefono(nuevosDatos.getTelefono());
        proveedor.setDireccion(nuevosDatos.getDireccion());
        proveedor.setEmail(nuevosDatos.getEmail());
        return proveedorRepository.save(proveedor);
    }
    return null;
}

}
