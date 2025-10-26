package com.inventorysystem_project.dtos;

import com.inventorysystem_project.entities.enums.EstadoProblema; // Importa el enum

public class ErrorConocidoDTO {
    private Long id;
    private String descripcionError;
    private String sintomas;
    private String causaRaiz;
    private String solucionTemporal;
    private String solucionDefinitivaPropuesta;
    private EstadoProblema estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getCausaRaiz() {
        return causaRaiz;
    }

    public void setCausaRaiz(String causaRaiz) {
        this.causaRaiz = causaRaiz;
    }

    public String getSolucionTemporal() {
        return solucionTemporal;
    }

    public void setSolucionTemporal(String solucionTemporal) {
        this.solucionTemporal = solucionTemporal;
    }

    public String getSolucionDefinitivaPropuesta() {
        return solucionDefinitivaPropuesta;
    }

    public void setSolucionDefinitivaPropuesta(String solucionDefinitivaPropuesta) {
        this.solucionDefinitivaPropuesta = solucionDefinitivaPropuesta;
    }

    public EstadoProblema getEstado() {
        return estado;
    }

    public void setEstado(EstadoProblema estado) {
        this.estado = estado;
    }
}