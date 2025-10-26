package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.ComentarioTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioTicketRepository extends JpaRepository<ComentarioTicket, Long> {
    
    // Buscar todos los comentarios de un ticket específico
    List<ComentarioTicket> findByTicketId(Long ticketId);
}