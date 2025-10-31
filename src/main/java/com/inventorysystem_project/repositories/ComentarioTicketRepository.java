package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.ComentarioTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioTicketRepository extends JpaRepository<ComentarioTicket, Long> {
    
    // Buscar todos los comentarios de un ticket específico
    List<ComentarioTicket> findByTicketId(Long ticketId);
    
    // --- AÑADIR ESTE MÉTODO PARA ORDENAR POR FECHA ---
    // Spring Data JPA lo implementará automáticamente basado en el nombre
    // "findByTicketId..." busca por el campo 'ticket' y su 'id'
    // "...OrderByFechaCreacionDesc" usa tu campo 'fechaCreacion' para ordenar
    List<ComentarioTicket> findByTicketIdOrderByFechaCreacionDesc(Long ticketId);
    // --- FIN DEL MÉTODO AÑADIDO ---
}