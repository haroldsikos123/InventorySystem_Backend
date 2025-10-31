package com.inventorysystem_project.dtos;

import java.time.LocalDateTime;

public class ActividadCombinadaDTO {
    
    private String tipo; // "comentario" o "actividad"
    private String descripcion;
    private String usuarioNombre;
    private LocalDateTime fecha;

    // Constructor
    public ActividadCombinadaDTO(String tipo, String descripcion, String usuarioNombre, LocalDateTime fecha) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.usuarioNombre = usuarioNombre;
        this.fecha = fecha;
    }

    // Constructor vacío
    public ActividadCombinadaDTO() {}

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}