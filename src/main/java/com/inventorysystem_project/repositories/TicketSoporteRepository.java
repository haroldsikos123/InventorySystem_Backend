package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.TicketSoporte;
import com.inventorysystem_project.entities.enums.EstadoTicket; // Importa el enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {
    
    // Búsquedas personalizadas
    List<TicketSoporte> findByEstado(EstadoTicket estado);
    List<TicketSoporte> findByUsuarioReportaId(Long usuarioId);
    List<TicketSoporte> findByResponsableAsignadoId(Long usuarioId);
    
    /**
     * Busca tickets por el ID del usuario que reportó Y por estado.
     */
    List<TicketSoporte> findByUsuarioReportaIdAndEstado(Long usuarioId, EstadoTicket estado);
}