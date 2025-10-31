package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.TicketActividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketActividadRepository extends JpaRepository<TicketActividad, Long> {
    
    // Método para buscar por ticket, ordenado por fecha descendente
    List<TicketActividad> findByTicketSoporteIdOrderByFechaActividadDesc(Long ticketId);
}