package com.polisong.polisong_marketplace.service;

import com.polisong.polisong_marketplace.model.Venta;
import com.polisong.polisong_marketplace.model.Proveedor;
import com.polisong.polisong_marketplace.model.Pedido;
import com.polisong.polisong_marketplace.repository.VentaRepository;
import com.polisong.polisong_marketplace.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProveedorRepository proveedorRepository;

    public VentaService(VentaRepository ventaRepository, ProveedorRepository proveedorRepository) {
        this.ventaRepository = ventaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // =============================
    // CRUD básico
    // =============================

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

    // =============================
    // Registrar nueva venta
    // =============================

    public Venta registrarVenta(Venta venta) {
        if (venta.getEstado() == null) {
            venta.setEstado("Pendiente");
        }
        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDate.now());
        }
        // Calcular ingreso total si viene pedido
        if (venta.getPedido() != null) {
            venta.setIngresoTotal(calcularIngresoDesdePedido(venta.getPedido()));
        }
        return ventaRepository.save(venta);
    }

    // =============================
    // Consultar todas las ventas
    // =============================

    public List<Venta> consultarVentas() {
        return ventaRepository.findAll();
    }

    // =============================
    // Actualizar estado
    // =============================

    public String actualizarEstadoVenta(Integer idVenta, String nuevoEstado) {
        Optional<Venta> ventaOpt = ventaRepository.findById(idVenta);
        if (ventaOpt.isEmpty()) {
            return "Venta no encontrada con ID: " + idVenta;
        }
        Venta venta = ventaOpt.get();
        venta.setEstado(nuevoEstado);
        ventaRepository.save(venta);
        return "Estado actualizado a: " + nuevoEstado;
    }

    // =============================
    // Actualizar proveedor (idProveedor pertenece a Proveedor con rol enum)
    // =============================

    public String actualizarVentaProveedor(Integer idVenta, Integer idProveedor) {
        Optional<Venta> ventaOpt = ventaRepository.findById(idVenta);
        if (ventaOpt.isEmpty()) {
            return "Venta no encontrada con ID: " + idVenta;
        }
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(idProveedor);
        if (proveedorOpt.isEmpty()) {
            return "Proveedor no encontrado con ID: " + idProveedor;
        }
        Venta venta = ventaOpt.get();
        venta.setProveedor(proveedorOpt.get());
        ventaRepository.save(venta);
        return "Proveedor actualizado correctamente para la venta ID: " + idVenta;
    }

    // =============================
    // Generar reporte de ventas
    // =============================

    public List<Venta> generarReporteVentas(Integer idProveedor) {
        if (idProveedor != null) {
            return ventaRepository.findByProveedor_IdProveedor(idProveedor);
        }
        return ventaRepository.findAll();
    }

    // =============================
    // Notificar al proveedor (simple log)
    // =============================

    public String notificarProveedorDesdePagina(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada con ID: " + idVenta));
        if (venta.getProveedor() == null) {
            return "⚠ No se pudo notificar: la venta no tiene proveedor asignado.";
        }
        String productos = obtenerResumenProductos(venta.getPedido());
        String mensaje = "Notificación de venta:\n" +
                "Proveedor: " + venta.getProveedor().getNombreProveedor() + "\n" +
                "Fecha: " + venta.getFechaVenta() + "\n" +
                "Estado: " + venta.getEstado() + "\n" +
                "Productos: " + productos + "\n" +
                "Ingreso Total: " + (venta.getIngresoTotal() != null ? venta.getIngresoTotal() : "N/D");
        System.out.println(mensaje);
        return mensaje;
    }

    // =============================
    // Helpers
    // =============================

    private BigDecimal calcularIngresoDesdePedido(Pedido pedido) {
        if (pedido == null || pedido.getDetalles() == null) {
            return BigDecimal.ZERO;
        }
        return pedido.getDetalles().stream()
                .map(d -> d.getSubtotal() == null ? BigDecimal.ZERO : BigDecimal.valueOf(d.getSubtotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String obtenerResumenProductos(Pedido pedido) {
        if (pedido == null) {
            return "Sin detalles";
        }
        int cantidad = (pedido.getDetalles() == null) ? 0 : pedido.getDetalles().size();
        Integer idPedido = pedido.getIdPedido();
        return "Pedido #" + (idPedido != null ? idPedido : "N/D") + " (" + cantidad + " ítems)";
    }
}


