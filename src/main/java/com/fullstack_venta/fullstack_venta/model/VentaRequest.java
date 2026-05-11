package com.fullstack_venta.fullstack_venta.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO que recibe el frontend al confirmar una compra.
 * Contiene el id del usuario y la lista de ítems del carrito.
 */
public class VentaRequest {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;

    @NotEmpty(message = "El carrito no puede estar vacío")
    @Valid
    private List<ItemRequest> items;

    public VentaRequest() {}

    public Long getUsuarioId()                   { return usuarioId; }
    public void setUsuarioId(Long usuarioId)     { this.usuarioId = usuarioId; }

    public List<ItemRequest> getItems()          { return items; }
    public void setItems(List<ItemRequest> items){ this.items = items; }

    // ---- Clase interna para cada ítem del carrito ----
    public static class ItemRequest {

        @NotNull(message = "El id del producto es obligatorio")
        private Long productoId;

        private String nombreProducto;

        @NotNull(message = "La cantidad es obligatoria")
        private Integer cantidad;

        @NotNull(message = "El precio unitario es obligatorio")
        private Double precioUnitario;

        public ItemRequest() {}

        public Long getProductoId()                    { return productoId; }
        public void setProductoId(Long productoId)     { this.productoId = productoId; }

        public String getNombreProducto()              { return nombreProducto; }
        public void setNombreProducto(String n)        { this.nombreProducto = n; }

        public Integer getCantidad()                   { return cantidad; }
        public void setCantidad(Integer cantidad)      { this.cantidad = cantidad; }

        public Double getPrecioUnitario()              { return precioUnitario; }
        public void setPrecioUnitario(Double p)        { this.precioUnitario = p; }
    }
}
