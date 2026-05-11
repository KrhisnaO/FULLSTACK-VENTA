package com.fullstack_venta.fullstack_venta.service;

import com.fullstack_venta.fullstack_venta.model.Venta;
import com.fullstack_venta.fullstack_venta.model.VentaRequest;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de ventas.
 */
public interface VentaService {

    /** Registra una nueva venta a partir del request del carrito. */
    Venta registrar(VentaRequest request);

    /** Retorna todas las ventas registradas. */
    List<Venta> listar();

    /** Busca una venta por su id. */
    Optional<Venta> buscarPorId(Long id);

    /** Retorna todas las ventas de un usuario. */
    List<Venta> buscarPorUsuario(Long usuarioId);

    /** Cancela una venta existente (cambia estado a CANCELADA). */
    Venta cancelar(Long id);
}
