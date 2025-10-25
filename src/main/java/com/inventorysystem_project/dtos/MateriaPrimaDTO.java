package com.inventorysystem_project.dtos;

public class MateriaPrimaDTO {

    private Long id;
    private String nombre;
    private String unidad;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {this.nombre = nombre;
    }
    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
