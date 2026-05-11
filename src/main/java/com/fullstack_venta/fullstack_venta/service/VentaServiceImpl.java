package com.fullstack_venta.fullstack_venta.service;

import com.fullstack_venta.fullstack_venta.exception.ResourceNotFoundException;
import com.fullstack_venta.fullstack_venta.exception.SolicitudInvalidaException;
import com.fullstack_venta.fullstack_venta.model.DetalleVenta;
import com.fullstack_venta.fullstack_venta.model.Venta;
import com.fullstack_venta.fullstack_venta.model.VentaRequest;
import com.fullstack_venta.fullstack_venta.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de ventas.
 * Aplica la lógica de negocio: calcula el total, construye los detalles
 * y persiste la venta en la base de datos Oracle.
 */
@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional
    public Venta registrar(VentaRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new SolicitudInvalidaException("El carrito no puede estar vacío");
        }

        Venta venta = new Venta();
        venta.setUsuarioId(request.getUsuarioId());
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("CONFIRMADA");

        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0.0;

        for (VentaRequest.ItemRequest item : request.getItems()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProductoId(item.getProductoId());
            detalle.setNombreProducto(
                item.getNombreProducto() != null ? item.getNombreProducto() : "Producto"
            );
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(item.getCantidad() * item.getPrecioUnitario());
            detalles.add(detalle);
            total += detalle.getSubtotal();
        }

        venta.setTotal(Math.round(total * 100.0) / 100.0);
        venta.setDetalles(detalles);

        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public List<Venta> buscarPorUsuario(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public Venta cancelar(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venta no encontrada con id: " + id));

        if ("CANCELADA".equals(venta.getEstado())) {
            throw new SolicitudInvalidaException("La venta ya está cancelada");
        }

        venta.setEstado("CANCELADA");
        return ventaRepository.save(venta);
    }
}
