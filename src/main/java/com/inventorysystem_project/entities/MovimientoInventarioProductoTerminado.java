package com.inventorysystem_project.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class MovimientoInventarioProductoTerminado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "almacen_id")
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "producto_terminado_id")
    private ProductoTerminado productoTerminado;

    private String tipoMovimiento;

    @Temporal(TemporalType.TIMESTAMP) // <-- CAMBIAR DE DATE A TIMESTAMP
    private Date fechaMovimiento;

    private Integer cantidad;

    private String motivo;

    @Column(name = "estado_entrega", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean estadoEntrega = false;  // Establecemos el valor por defecto como false

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public ProductoTerminado getProductoTerminado() {
        return productoTerminado;
    }

    public void setProductoTerminado(ProductoTerminado productoTerminado) {
        this.productoTerminado = productoTerminado;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Boolean getEstadoEntrega() {
        return estadoEntrega;
    }

    public void setEstadoEntrega(Boolean estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }
}
