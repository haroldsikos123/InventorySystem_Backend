package com.inventorysystem_project.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class StockAlmacenProductoTerminado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stockActual;
    private int stockMinimo;

    @Temporal(TemporalType.DATE)
    private Date ultimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "almacen_id", referencedColumnName = "id")
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "producto_terminado_id", referencedColumnName = "id")
    private ProductoTerminado productoTerminado;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
