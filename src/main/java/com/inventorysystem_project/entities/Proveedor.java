package com.inventorysystem_project.entities;

import jakarta.persistence.*;

@Entity
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_empresa_proveedor")
    private String nombreEmpresaProveedor;

    private Long ruc;

    private String direccion;

    private String telefono;

    private String correo;

    private String pais;

    @Column(name = "nombre_contacto")
    private String nombreContacto;

    private Boolean enabled;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresaProveedor() {
        return nombreEmpresaProveedor;
    }

    public void setNombreEmpresaProveedor(String nombreEmpresaProveedor) {
        this.nombreEmpresaProveedor = nombreEmpresaProveedor;
    }

    public Long getRuc() {
        return ruc;
    }

    public void setRuc(Long ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
