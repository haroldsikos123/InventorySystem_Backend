package com.inventorysystem_project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlmacenDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long empresaId; // Solo el ID de la empresa
    private String nombre;
    private String ubicacion;

    // Getters y Setters
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
}
