package com.fullstack_venta.fullstack_venta.repository;

import com.fullstack_venta.fullstack_venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Venta.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /** Obtiene todas las ventas de un usuario específico. */
    List<Venta> findByUsuarioId(Long usuarioId);
}
