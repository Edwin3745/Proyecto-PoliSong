package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.model.Cancion;
import com.polisong.polisong_marketplace.model.Proveedor;
import com.polisong.polisong_marketplace.model.Venta;
import com.polisong.polisong_marketplace.model.Vinilo;
import com.polisong.polisong_marketplace.model.Role;
import com.polisong.polisong_marketplace.repository.ProveedorRepository;
import com.polisong.polisong_marketplace.repository.VentaRepository;
import com.polisong.polisong_marketplace.repository.CancionRepository;
import com.polisong.polisong_marketplace.repository.ViniloRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ViniloRepository viniloRepository;
    private final VentaRepository ventaRepository;
    private final CancionRepository cancionRepository;

    public ProveedorService(ProveedorRepository proveedorRepository,
                            ViniloRepository viniloRepository,
                            VentaRepository ventaRepository,
                            CancionRepository cancionRepository) {
        this.proveedorRepository = proveedorRepository;
        this.viniloRepository = viniloRepository;
        this.ventaRepository = ventaRepository;
        this.cancionRepository = cancionRepository;
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

    // Publicar vinilo asociado al proveedor
    public Vinilo publicarVinilo(Integer idProveedor, Vinilo vinilo) {
        Proveedor proveedor = buscarPorId(idProveedor);
        if (proveedor == null) throw new IllegalArgumentException("Proveedor no encontrado");
        vinilo.setProveedor(proveedor);
        if (vinilo.getEstado() == null) vinilo.setEstado("ACTIVO");
        return viniloRepository.save(vinilo);
    }

    // Publicar canción (gratis si precio == null o 0)
    public Cancion publicarCancion(Integer idProveedor, Cancion cancion) {
        Proveedor proveedor = buscarPorId(idProveedor);
        if (proveedor == null) throw new IllegalArgumentException("Proveedor no encontrado");
        cancion.setProveedor(proveedor);
        if (cancion.getEstado() == null) cancion.setEstado("ACTIVO");
        // Normalizar gratis/pago: si precio null -> 0
        if (cancion.getPrecio() == null) cancion.setPrecio(0.0);
        return cancionRepository.save(cancion);
    }

    // Editar vinilo (verifica propiedad)
    public Vinilo editarVinilo(Integer idProveedor, Integer idVinilo, Vinilo cambios) {
        Vinilo existente = viniloRepository.findById(idVinilo).orElse(null);
        if (existente == null || existente.getProveedor() == null || !Objects.equals(existente.getProveedor().getIdProveedor(), idProveedor)) {
            throw new IllegalArgumentException("Vinilo no pertenece al proveedor");
        }
        if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
        if (cambios.getArtista() != null) existente.setArtista(cambios.getArtista());
        if (cambios.getAñoLanzamiento() != null) existente.setAñoLanzamiento(cambios.getAñoLanzamiento());
        if (cambios.getPrecio() != null) existente.setPrecio(cambios.getPrecio());
        if (cambios.getCantidadDisponible() != null) existente.setCantidadDisponible(cambios.getCantidadDisponible());
        if (cambios.getEstado() != null) existente.setEstado(cambios.getEstado());
        return viniloRepository.save(existente);
    }

    // Editar canción (verifica propiedad)
    public Cancion editarCancion(Integer idProveedor, Integer idCancion, Cancion cambios) {
        Cancion existente = cancionRepository.findById(idCancion).orElse(null);
        if (existente == null || existente.getProveedor() == null || !Objects.equals(existente.getProveedor().getIdProveedor(), idProveedor)) {
            throw new IllegalArgumentException("Canción no pertenece al proveedor");
        }
        if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
        if (cambios.getPrecio() != null) existente.setPrecio(cambios.getPrecio());
        if (cambios.getEstado() != null) existente.setEstado(cambios.getEstado());
        if (cambios.getDuracion() != null) existente.setDuracion(cambios.getDuracion());
        if (cambios.getCalidadKbps() != null) existente.setCalidadKbps(cambios.getCalidadKbps());
        return cancionRepository.save(existente);
    }

    // Inventario combinado (vinilos + canciones)
    public Map<String,Object> inventarioCompleto(Integer idProveedor) {
        Proveedor proveedor = buscarPorId(idProveedor);
        if (proveedor == null) throw new IllegalArgumentException("Proveedor no encontrado");
        List<Vinilo> vinilos = verProductos(idProveedor);
        List<Cancion> canciones = verCanciones(idProveedor);
        Map<String,Object> resp = new LinkedHashMap<>();
        resp.put("vinilos", vinilos);
        resp.put("canciones", canciones);
        resp.put("total", vinilos.size() + canciones.size());
        return resp;
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

    // Ver productos del proveedor 
    public List<Vinilo> verProductos(Integer idProveedor) {
        Proveedor proveedor = buscarPorId(idProveedor);
        if (proveedor == null) return Collections.emptyList();
        //Si la relación está cargada
        if (proveedor.getVinilos() != null && !proveedor.getVinilos().isEmpty()) {
            return proveedor.getVinilos();
        }
        //filtrar desde repositorio 
        List<Vinilo> todos = viniloRepository.findAll();
        List<Vinilo> resultado = new ArrayList<>();
        for (Vinilo v : todos) {
            if (v.getProveedor() != null && Objects.equals(v.getProveedor().getIdProveedor(), idProveedor)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // Canciones del proveedor
    public List<Cancion> verCanciones(Integer idProveedor) {
        List<Cancion> todas = cancionRepository.findAll();
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion c : todas) {
            if (c.getProveedor() != null && Objects.equals(c.getProveedor().getIdProveedor(), idProveedor)) {
                resultado.add(c);
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

