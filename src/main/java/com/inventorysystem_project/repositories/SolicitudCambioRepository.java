package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.SolicitudCambio;
import com.inventorysystem_project.entities.enums.TipoCambio; // Importa el enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudCambioRepository extends JpaRepository<SolicitudCambio, Long> {

    // Búsquedas personalizadas
    List<SolicitudCambio> findByTipoCambio(TipoCambio tipoCambio);
    List<SolicitudCambio> findBySolicitanteId(Long solicitanteId);
    // List<SolicitudCambio> findByEstado(EstadoCambio estado);
}