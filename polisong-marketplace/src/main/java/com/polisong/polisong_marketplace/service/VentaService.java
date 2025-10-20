package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Valoracion;
import com.polisong.polisong_marketplace.model.Venta;
import com.polisong.polisong_marketplace.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;


    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }
    
    //Registrar nueva venta
    public Venta registrarVenta(Venta venta) {
        venta.setEstado("Pendiente"); //Estado inicial por defecto
        return ventaRepository.save(venta);
    }

    // Consultar todas las ventas
    public List<Venta> consultarVentas() {
        return ventaRepository.findAll();
    }

    // Actualizar estado de una venta (por ID)
    public String actualizarEstadoVenta(Integer idVenta, String nuevoEstado) {
        Optional<Venta> ventaOpt = ventaRepository.findById(idVenta);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            venta.setEstado(nuevoEstado);
            ventaRepository.save(venta);
            return "Estado de la venta actualizado correctamente a: " + nuevoEstado;
        } else {
            return "Venta no encontrada con ID: " + idVenta;
        }
    }

    // Actualizar proveedor de una venta
    public String actualizarVentaProveedor(Integer idVenta, Integer idProveedor) {
        Optional<Venta> ventaOpt = ventaRepository.findById(idVenta);
        if (ventaOpt.isPresent()) {
            Venta venta = ventaOpt.get();
            venta.getProveedor().setIdProveedor(idProveedor);
            ventaRepository.save(venta);
            return "Proveedor actualizado correctamente para la venta ID: " + idVenta;
        } else {
            return "Venta no encontrada con ID: " + idVenta;
        }
    }
}
