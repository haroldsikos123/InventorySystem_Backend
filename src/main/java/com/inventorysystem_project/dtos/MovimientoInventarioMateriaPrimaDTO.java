package com.inventorysystem_project.dtos;

import java.util.Date;

public class MovimientoInventarioMateriaPrimaDTO {

    private Long id;
    private Long almacenId;
    private Long materiaPrimaId;
    private Date fechaMovimiento;
    private String tipoMovimiento;
    private Integer cantidad;
    private String motivo;
    private Boolean estadoRecepcion;  // Aquí añadimos el nuevo campo

    // Getters y Setters

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

    public Long getMateriaPrimaId() {
        return materiaPrimaId;
    }

    public void setMateriaPrimaId(Long materiaPrimaId) {
        this.materiaPrimaId = materiaPrimaId;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
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

    public Boolean getEstadoRecepcion() {
        return estadoRecepcion;
    }

    public void setEstadoRecepcion(Boolean estadoRecepcion) {
        this.estadoRecepcion = estadoRecepcion;
    }
}
