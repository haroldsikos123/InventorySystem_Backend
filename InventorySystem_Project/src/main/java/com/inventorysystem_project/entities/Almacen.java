package com.inventorysystem_project.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "Almacen")
public class Almacen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    @JsonBackReference
    private Empresa empresa;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String ubicacion;

    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MovimientoInventarioMateriaPrima> movimientosInventario;

    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StockAlmacenMateriaPrima> stockMateriaPrima;

    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StockAlmacenProductoTerminado> stockProductoTerminado;

    @OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MovimientoInventarioProductoTerminado> ventasProductoTerminado;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<MovimientoInventarioMateriaPrima> getMovimientosInventario() {
        return movimientosInventario;
    }

    public void setMovimientosInventario(List<MovimientoInventarioMateriaPrima> movimientosInventario) {
        this.movimientosInventario = movimientosInventario;
    }

    public List<StockAlmacenMateriaPrima> getStockMateriaPrima() {
        return stockMateriaPrima;
    }

    public void setStockMateriaPrima(List<StockAlmacenMateriaPrima> stockMateriaPrima) {
        this.stockMateriaPrima = stockMateriaPrima;
    }

    public List<StockAlmacenProductoTerminado> getStockProductoTerminado() {
        return stockProductoTerminado;
    }

    public void setStockProductoTerminado(List<StockAlmacenProductoTerminado> stockProductoTerminado) {
        this.stockProductoTerminado = stockProductoTerminado;
    }

    public List<MovimientoInventarioProductoTerminado> getVentasProductoTerminado() {
        return ventasProductoTerminado;
    }

    public void setVentasProductoTerminado(List<MovimientoInventarioProductoTerminado> ventasProductoTerminado) {
        this.ventasProductoTerminado = ventasProductoTerminado;
    }
}
