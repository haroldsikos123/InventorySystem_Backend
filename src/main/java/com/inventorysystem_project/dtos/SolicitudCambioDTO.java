package com.inventorysystem_project.dtos;

// Importa los enums
import com.inventorysystem_project.entities.enums.EstadoCambio;
import com.inventorysystem_project.entities.enums.TipoCambio;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SolicitudCambioDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String formattedId; // ID formateado como #CAB-123
    private String titulo;
    private String descripcion;
    private String justificacion;
    
    private Long solicitanteId; // ID del solicitante
    private String solicitanteNombre; // Campo extra para mostrar en el front
    
    private String impacto;
    private String riesgos;
    private String planImplementacion;
    private String planRetirada;
    private EstadoCambio estado;
    private TipoCambio tipoCambio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public String getSolicitanteNombre() {
        return solicitanteNombre;
    }

    public void setSolicitanteNombre(String solicitanteNombre) {
        this.solicitanteNombre = solicitanteNombre;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public String getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(String riesgos) {
        this.riesgos = riesgos;
    }

    public String getPlanImplementacion() {
        return planImplementacion;
    }

    public void setPlanImplementacion(String planImplementacion) {
        this.planImplementacion = planImplementacion;
    }

    public String getPlanRetirada() {
        return planRetirada;
    }

    public void setPlanRetirada(String planRetirada) {
        this.planRetirada = planRetirada;
    }

    public EstadoCambio getEstado() {
        return estado;
    }

    public void setEstado(EstadoCambio estado) {
        this.estado = estado;
    }

    public TipoCambio getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(TipoCambio tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getFormattedId() {
        return formattedId;
    }

    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }
}