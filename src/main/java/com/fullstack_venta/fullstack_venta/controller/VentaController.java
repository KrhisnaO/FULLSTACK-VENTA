package com.fullstack_venta.fullstack_venta.controller;

import com.fullstack_venta.fullstack_venta.model.Venta;
import com.fullstack_venta.fullstack_venta.model.VentaRequest;
import com.fullstack_venta.fullstack_venta.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del microservicio de ventas.
 * Expone los endpoints para registrar, consultar y cancelar ventas.
 *
 * Base URL : /api/ventas
 * Puerto   : 8082
 */
@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    /**
     * POST /api/ventas
     * Registra una nueva venta al confirmar la compra desde el carrito.
     */
    @PostMapping
    public ResponseEntity<Venta> registrar(@Valid @RequestBody VentaRequest request) {
        Venta nuevaVenta = ventaService.registrar(request);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    /**
     * GET /api/ventas
     * Lista todas las ventas registradas (uso admin).
     */
    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    /**
     * GET /api/ventas/{id}
     * Obtiene una venta por su id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        return ventaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/ventas/usuario/{usuarioId}
     * Obtiene el historial de compras de un usuario.
     */
    @GetMapping("/usuario/{usuarioId}")
    public List<Venta> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ventaService.buscarPorUsuario(usuarioId);
    }

    /**
     * PATCH /api/ventas/{id}/cancelar
     * Cancela una venta existente.
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Venta> cancelar(@PathVariable Long id) {
        try {
            Venta cancelada = ventaService.cancelar(id);
            return ResponseEntity.ok(cancelada);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}