package com.inventorysystem_project.entities;

import jakarta.persistence.*;

@Entity
public class ProveedorMateriaPrima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "materia_prima_id", referencedColumnName = "id")
    private MateriaPrima materiaPrima;

    @ManyToOne
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id")
    private Proveedor proveedor;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}
