package com.inventorysystem_project.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "Empresa")
public class Empresa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Long ruc;

    @Column(length = 50, nullable = false)
    private String direccion;

    @Column(length = 50, nullable = false)
    private String telefono;

    @Column(length = 50, nullable = false)
    private String correo;

    @Column(length = 50, nullable = false)
    private String pais;

    private Boolean enabled;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cliente> clientes;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Almacen> almacenes;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrdenCompra> ordenesCompra;

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

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Almacen> getAlmacenes() {
        return almacenes;
    }

    public void setAlmacenes(List<Almacen> almacenes) {
        this.almacenes = almacenes;
    }

    public List<OrdenCompra> getOrdenesCompra() {
        return ordenesCompra;
    }

    public void setOrdenesCompra(List<OrdenCompra> ordenesCompra) {
        this.ordenesCompra = ordenesCompra;
    }
}