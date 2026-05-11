package com.fullstack_venta.fullstack_venta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una venta/pedido realizado por un usuario.
 * Almacena el id del usuario, la fecha, el total y el estado del pedido.
 */
@Entity
@Table(name = "VENTAS")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del usuario es obligatorio")
    @Column(name = "USUARIO_ID", nullable = false)
    private Long usuarioId;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor a 0")
    @Column(name = "TOTAL", nullable = false)
    private Double total;

    @Column(name = "ESTADO", nullable = false, length = 30)
    private String estado; // CONFIRMADA, CANCELADA

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}

    public Venta(Long id, Long usuarioId, LocalDateTime fecha, Double total, String estado) {
        this.id        = id;
        this.usuarioId = usuarioId;
        this.fecha     = fecha;
        this.total     = total;
        this.estado    = estado;
    }

    @PrePersist
    public void prePersist() {
        if (this.fecha  == null) this.fecha  = LocalDateTime.now();
        if (this.estado == null) this.estado = "CONFIRMADA";
    }

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public Long getUsuarioId()                   { return usuarioId; }
    public void setUsuarioId(Long usuarioId)     { this.usuarioId = usuarioId; }

    public LocalDateTime getFecha()              { return fecha; }
    public void setFecha(LocalDateTime fecha)    { this.fecha = fecha; }

    public Double getTotal()                     { return total; }
    public void setTotal(Double total)           { this.total = total; }

    public String getEstado()                    { return estado; }
    public void setEstado(String estado)         { this.estado = estado; }

    public List<DetalleVenta> getDetalles()      { return detalles; }
    public void setDetalles(List<DetalleVenta> d){ this.detalles = d; }
}
