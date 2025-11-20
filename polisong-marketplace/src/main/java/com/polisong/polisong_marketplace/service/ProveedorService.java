package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Proveedor;
import com.polisong.polisong_marketplace.model.Venta;
import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.model.Role;
import com.polisong.polisong_marketplace.repository.ProveedorRepository;
import com.polisong.polisong_marketplace.repository.VentaRepository;
import com.polisong.polisong_marketplace.repository.ViniloRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ViniloRepository viniloRepository;
    private final VentaRepository ventaRepository;

    public ProveedorService(ProveedorRepository proveedorRepository,
                            ViniloRepository viniloRepository,
                            VentaRepository ventaRepository) {
        this.proveedorRepository = proveedorRepository;
        this.viniloRepository = viniloRepository;
        this.ventaRepository = ventaRepository;
    }

    // Listar todos los proveedores
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    // Consultar un proveedor por ID
    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    // Registrar proveedor (semántica específica)
    public String registrar(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
        return "Proveedor registrado correctamente.";
    }

    // Registro completo simplificado: solo proveedor autónomo
    public Proveedor registrarAutonomo(String aliasContacto, String correo, String contrasena) {
        Proveedor proveedor = new Proveedor();
        proveedor.setAliasContacto(aliasContacto);
        proveedor.setCorreo(correo);
        proveedor.setContrasena(contrasena); // TODO: cifrar
        proveedor.setRol(Role.PROVEEDOR);
        return proveedorRepository.save(proveedor);
    }

    public Proveedor login(String correo, String contrasena) {
        Optional<Proveedor> opt = proveedorRepository.findByCorreo(correo);
        if (opt.isPresent()) {
            Proveedor p = opt.get();
            if (p.getContrasena() != null && p.getContrasena().equals(contrasena)) {
                return p;
            }
        }
        return null;
    }

    // Guardar genérico (ya existente)
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Actualizar datos del proveedor
    public String actualizar(Integer id, Proveedor cambios) {
        Proveedor existente = buscarPorId(id);
        if (existente == null) {
            return "Proveedor no encontrado.";
        }
        if (cambios.getAliasContacto() != null) existente.setAliasContacto(cambios.getAliasContacto());
        if (cambios.getCorreo() != null) existente.setCorreo(cambios.getCorreo());
        if (cambios.getContrasena() != null) existente.setContrasena(cambios.getContrasena());
        if (cambios.getRol() != null) existente.setRol(cambios.getRol());
        proveedorRepository.save(existente);
        return "Proveedor actualizado correctamente.";
    }

    // Eliminar
    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }

    // Ver productos del proveedor (solo vinilos, ya que Cancion no tiene relación con proveedor)
    public List<Vinilo> verProductos(Integer idProveedor) {
        Proveedor proveedor = buscarPorId(idProveedor);
        if (proveedor == null) return Collections.emptyList();
        // Si la relación está cargada
        if (proveedor.getVinilos() != null && !proveedor.getVinilos().isEmpty()) {
            return proveedor.getVinilos();
        }
        // Fallback: filtrar desde repositorio (si en algún momento se cambia a lazy sin fetch join)
        List<Vinilo> todos = viniloRepository.findAll();
        List<Vinilo> resultado = new ArrayList<>();
        for (Vinilo v : todos) {
            if (v.getProveedor() != null && Objects.equals(v.getProveedor().getIdProveedor(), idProveedor)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // Ver historial de pedidos asociados a ventas del proveedor
    public List<Pedido> verHistorialPedidos(Integer idProveedor) {
        List<Venta> ventas = ventaRepository.findByProveedor_IdProveedor(idProveedor);
        List<Pedido> pedidos = new ArrayList<>();
        for (Venta v : ventas) {
            if (v.getPedido() != null) {
                pedidos.add(v.getPedido());
            }
        }
        return pedidos;
    }

    // Estadísticas de ventas del proveedor
    public Map<String, Object> estadisticasVenta(Integer idProveedor) {
        List<Venta> ventas = ventaRepository.findByProveedor_IdProveedor(idProveedor);
        int totalVentas = ventas.size();
        BigDecimal totalIngresos = ventas.stream()
                .map(Venta::getIngresoTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> ventasPorEstado = new HashMap<>();
        for (Venta v : ventas) {
            String estado = v.getEstado() != null ? v.getEstado() : "Sin Estado";
            ventasPorEstado.put(estado, ventasPorEstado.getOrDefault(estado, 0L) + 1);
        }

    BigDecimal promedioIngreso = totalVentas > 0 ?
        totalIngresos.divide(BigDecimal.valueOf(totalVentas), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("totalVentas", totalVentas);
        respuesta.put("totalIngresos", totalIngresos);
        respuesta.put("promedioIngreso", promedioIngreso);
        respuesta.put("ventasPorEstado", ventasPorEstado);
        return respuesta;
    }
}

