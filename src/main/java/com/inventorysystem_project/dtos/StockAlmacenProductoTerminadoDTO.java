package com.inventorysystem_project.dtos;

import java.util.Date;

public class StockAlmacenProductoTerminadoDTO {

    private Long id;
    private Long almacenId;
    private Long productoTerminadoId;
    private int stockActual;
    private int stockMinimo;
    private Date ultimaActualizacion;

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

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
