package com.fullstack_venta.fullstack_venta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack_venta.fullstack_venta.controller.VentaController;
import com.fullstack_venta.fullstack_venta.exception.ResourceNotFoundException;
import com.fullstack_venta.fullstack_venta.model.Venta;
import com.fullstack_venta.fullstack_venta.model.VentaRequest;
import com.fullstack_venta.fullstack_venta.service.VentaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para VentaController usando MockMvc.
 */
@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @Autowired
    private ObjectMapper objectMapper;

    private VentaRequest buildRequest() {
        VentaRequest req = new VentaRequest();
        req.setUsuarioId(1L);
        VentaRequest.ItemRequest item = new VentaRequest.ItemRequest();
        item.setProductoId(10L);
        item.setNombreProducto("Laptop");
        item.setCantidad(1);
        item.setPrecioUnitario(800.0);
        req.setItems(List.of(item));
        return req;
    }

    @Test
    void registrar_retorna201() throws Exception {
        Venta venta = new Venta(1L, 1L, LocalDateTime.now(), 800.0, "CONFIRMADA");
        when(ventaService.registrar(any(VentaRequest.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("CONFIRMADA"));
    }

    @Test
    void listar_retorna200ConListaDeVentas() throws Exception {
        Venta v1 = new Venta(1L, 1L, LocalDateTime.now(), 500.0, "CONFIRMADA");
        Venta v2 = new Venta(2L, 2L, LocalDateTime.now(), 300.0, "CONFIRMADA");
        when(ventaService.listar()).thenReturn(List.of(v1, v2));

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtenerPorId_existente_retorna200() throws Exception {
        Venta venta = new Venta(1L, 1L, LocalDateTime.now(), 800.0, "CONFIRMADA");
        when(ventaService.buscarPorId(1L)).thenReturn(Optional.of(venta));

        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerPorId_noExistente_retorna404() throws Exception {
        when(ventaService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorUsuario_retorna200ConVentas() throws Exception {
        Venta venta = new Venta(1L, 5L, LocalDateTime.now(), 300.0, "CONFIRMADA");
        when(ventaService.buscarPorUsuario(5L)).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/ventas/usuario/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void cancelar_existente_retorna200ConEstadoCancelada() throws Exception {
        Venta cancelada = new Venta(1L, 1L, LocalDateTime.now(), 800.0, "CANCELADA");
        when(ventaService.cancelar(1L)).thenReturn(cancelada);

        mockMvc.perform(patch("/api/ventas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADA"));
    }

    @Test
    void cancelar_noExistente_retorna404() throws Exception {
        when(ventaService.cancelar(99L))
                .thenThrow(new ResourceNotFoundException("Venta no encontrada con id: 99"));

        mockMvc.perform(patch("/api/ventas/99/cancelar"))
                .andExpect(status().isNotFound());
    }
}
