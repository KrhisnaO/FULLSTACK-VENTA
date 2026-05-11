package com.fullstack_venta.fullstack_venta;

import com.fullstack_venta.fullstack_venta.exception.ResourceNotFoundException;
import com.fullstack_venta.fullstack_venta.exception.SolicitudInvalidaException;
import com.fullstack_venta.fullstack_venta.model.Venta;
import com.fullstack_venta.fullstack_venta.model.VentaRequest;
import com.fullstack_venta.fullstack_venta.repository.VentaRepository;
import com.fullstack_venta.fullstack_venta.service.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para VentaServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private VentaRequest requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new VentaRequest();
        requestValido.setUsuarioId(1L);

        VentaRequest.ItemRequest item = new VentaRequest.ItemRequest();
        item.setProductoId(10L);
        item.setNombreProducto("Laptop");
        item.setCantidad(2);
        item.setPrecioUnitario(500.0);

        requestValido.setItems(List.of(item));
    }

    // ---- registrar() ----

    @Test
    void registrar_conDatosValidos_retornaVentaGuardada() {
        Venta ventaMock = new Venta(1L, 1L, LocalDateTime.now(), 1000.0, "CONFIRMADA");
        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaMock);

        Venta resultado = ventaService.registrar(requestValido);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    @Test
    void registrar_conCarritoVacio_lanzaSolicitudInvalidaException() {
        VentaRequest requestVacio = new VentaRequest();
        requestVacio.setUsuarioId(1L);
        requestVacio.setItems(new ArrayList<>());

        assertThrows(SolicitudInvalidaException.class,
                () -> ventaService.registrar(requestVacio));

        verify(ventaRepository, never()).save(any());
    }

    @Test
    void registrar_conItemsNull_lanzaSolicitudInvalidaException() {
        VentaRequest requestNull = new VentaRequest();
        requestNull.setUsuarioId(1L);
        requestNull.setItems(null);

        assertThrows(SolicitudInvalidaException.class,
                () -> ventaService.registrar(requestNull));
    }

    @Test
    void registrar_calculaTotalCorrectamente() {
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        Venta resultado = ventaService.registrar(requestValido);

        // 2 unidades * $500 = $1000
        assertEquals(1000.0, resultado.getTotal());
    }

    // ---- listar() ----

    @Test
    void listar_retornaTodasLasVentas() {
        List<Venta> ventas = List.of(
                new Venta(1L, 1L, LocalDateTime.now(), 500.0, "CONFIRMADA"),
                new Venta(2L, 2L, LocalDateTime.now(), 200.0, "CONFIRMADA")
        );
        when(ventaRepository.findAll()).thenReturn(ventas);

        List<Venta> resultado = ventaService.listar();

        assertEquals(2, resultado.size());
        verify(ventaRepository, times(1)).findAll();
    }

    // ---- buscarPorId() ----

    @Test
    void buscarPorId_existente_retornaVenta() {
        Venta venta = new Venta(1L, 1L, LocalDateTime.now(), 1000.0, "CONFIRMADA");
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Optional<Venta> resultado = ventaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void buscarPorId_noExistente_retornaEmpty() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Venta> resultado = ventaService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    // ---- buscarPorUsuario() ----

    @Test
    void buscarPorUsuario_retornaVentasDelUsuario() {
        List<Venta> ventas = List.of(
                new Venta(1L, 5L, LocalDateTime.now(), 300.0, "CONFIRMADA")
        );
        when(ventaRepository.findByUsuarioId(5L)).thenReturn(ventas);

        List<Venta> resultado = ventaService.buscarPorUsuario(5L);

        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getUsuarioId());
    }

    // ---- cancelar() ----

    @Test
    void cancelar_ventaExistente_cambiaEstadoACancelada() {
        Venta venta = new Venta(1L, 1L, LocalDateTime.now(), 1000.0, "CONFIRMADA");
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        Venta resultado = ventaService.cancelar(1L);

        assertEquals("CANCELADA", resultado.getEstado());
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void cancelar_ventaNoExistente_lanzaResourceNotFoundException() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ventaService.cancelar(99L));
    }

    @Test
    void cancelar_ventaYaCancelada_lanzaSolicitudInvalidaException() {
        Venta ventaCancelada = new Venta(1L, 1L, LocalDateTime.now(), 500.0, "CANCELADA");
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(ventaCancelada));

        assertThrows(SolicitudInvalidaException.class,
                () -> ventaService.cancelar(1L));
    }
}
