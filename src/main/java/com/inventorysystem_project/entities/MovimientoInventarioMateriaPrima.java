package com.inventorysystem_project.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "movimiento_inventario_materia_prima")
public class MovimientoInventarioMateriaPrima implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "almacen_id", referencedColumnName = "id")
    private Almacen almacen;

    @ManyToOne
    @JoinColumn(name = "materia_prima_id", referencedColumnName = "id")
    private MateriaPrima materiaPrima;

    @Column(name = "fecha_movimiento")
    @Temporal(TemporalType.TIMESTAMP) // <-- AÑADIR ESTA LÍNEA
    private Date fechaMovimiento;

    @Column(name = "tipo_movimiento", length = 50)
    private String tipoMovimiento;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "motivo", length = 50)
    private String motivo;

    @Column(name = "estado_recepcion", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean estadoRecepcion = false; // Establecemos el valor por defecto como false

    // Getters y Setters

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

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
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
