package com.polisong.polisong_marketplace.repository;

import com.polisong.polisong_marketplace.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {

    // Buscar reportes por el ID del administrador
    List<Reporte> findByAdministrador_IdAdmin(Integer idAdmin);

    // Filtrar por tipo (ignorar mayúsculas/minúsculas)
    List<Reporte> findByTipoIgnoreCase(String tipo);

    // Contar dentro de un rango de fechas
    long countByFechaGeneracionBetween(LocalDateTime inicio, LocalDateTime fin);
}
