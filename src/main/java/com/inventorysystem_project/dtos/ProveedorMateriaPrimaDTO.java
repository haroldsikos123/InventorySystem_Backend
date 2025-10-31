package com.inventorysystem_project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProveedorMateriaPrimaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long materiaPrimaId;
    private Long proveedorId;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMateriaPrimaId() {
        return materiaPrimaId;
    }

    public void setMateriaPrimaId(Long materiaPrimaId) {
        this.materiaPrimaId = materiaPrimaId;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }
}
