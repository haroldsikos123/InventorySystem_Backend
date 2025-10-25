package com.inventorysystem_project.dtos;

import com.inventorysystem_project.entities.DetalleOrdenCompra;

import java.util.Date;
import java.util.List;

public class OrdenCompraDTO {

    private Long id;
    private Long empresaId;
    private Long proveedorId;
    private Date fechaEmision;
    private String estado;
    private String codigoOrden;
    private List<DetalleOrdenCompraDTO> detalles;
    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public void setCodigoOrden(String codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public List<DetalleOrdenCompraDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenCompraDTO> detalles) {
        this.detalles = detalles;
    }
}
