package com.inventorysystem_project.entities;

import com.inventorysystem_project.entities.enums.EstadoProblema; // Importa el enum
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "error_conocido")
@NoArgsConstructor
@AllArgsConstructor
public class ErrorConocido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcionError;

    @Column(columnDefinition = "TEXT")
    private String sintomas;

    @Column(columnDefinition = "TEXT")
    private String causaRaiz;

    @Column(columnDefinition = "TEXT")
    private String solucionTemporal; // Workaround

    @Column(columnDefinition = "TEXT")
    private String solucionDefinitivaPropuesta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProblema estado;

    // Getters and Setters

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