package com.inventorysystem_project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inventorysystem_project.entities.Usuario;

public class RolDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String rol;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}





