package com.inventorysystem_project.dtos;

import java.util.Date;

public class MovimientoInventarioProductoTerminadoDTO {

    private Long id;
    private Long almacenId;
    private Long productoTerminadoId;
    private String tipoMovimiento;
    private Date fechaMovimiento;
    private Integer cantidad;
    private String motivo;
    private Boolean estadoEntrega;  // Aquí añadimos el nuevo campo

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlmacenId() {
        return almacenId;
    }

    public void setAlmacenId(Long almacenId) {
        this.almacenId = almacenId;
    }

    public Long getProductoTerminadoId() {
        return productoTerminadoId;
    }

    public void setProductoTerminadoId(Long productoTerminadoId) {
        this.productoTerminadoId = productoTerminadoId;
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
