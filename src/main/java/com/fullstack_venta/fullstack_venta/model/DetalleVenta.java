package com.fullstack_venta.fullstack_venta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Entidad que representa el detalle (línea) de una venta.
 * Contiene el producto comprado, la cantidad y el precio unitario al momento de la compra.
 */
@Entity
@Table(name = "DETALLE_VENTAS")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENTA_ID", nullable = false)
    @JsonIgnore
    private Venta venta;

    @NotNull(message = "El id del producto es obligatorio")
    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name = "NOMBRE_PRODUCTO", nullable = false, length = 100)
    private String nombreProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a 0")
    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private Double precioUnitario;

    @Column(name = "SUBTOTAL", nullable = false)
    private Double subtotal;

    public DetalleVenta() {}

    public DetalleVenta(Long id, Venta venta, Long productoId, String nombreProducto,
                        Integer cantidad, Double precioUnitario) {
        this.id             = id;
        this.venta          = venta;
        this.productoId     = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal       = cantidad * precioUnitario;
    }

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (this.cantidad != null && this.precioUnitario != null) {
            this.subtotal = this.cantidad * this.precioUnitario;
        }
    }

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public Venta getVenta()                          { return venta; }
    public void setVenta(Venta venta)                { this.venta = venta; }

    public Long getProductoId()                      { return productoId; }
    public void setProductoId(Long productoId)       { this.productoId = productoId; }

    public String getNombreProducto()                { return nombreProducto; }
    public void setNombreProducto(String n)          { this.nombreProducto = n; }

    public Integer getCantidad()                     { return cantidad; }
    public void setCantidad(Integer cantidad)        { this.cantidad = cantidad; }

    public Double getPrecioUnitario()                { return precioUnitario; }
    public void setPrecioUnitario(Double p)          { this.precioUnitario = p; }

    public Double getSubtotal()                      { return subtotal; }
    public void setSubtotal(Double subtotal)         { this.subtotal = subtotal; }
}
