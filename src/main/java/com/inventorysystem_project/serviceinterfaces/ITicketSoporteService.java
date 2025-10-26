package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.dtos.ComentarioTicketDTO;
import com.inventorysystem_project.dtos.TicketSoporteDTO;
import java.util.List;

public interface ITicketSoporteService {
    
    // --- Gestión de Tickets ---
    TicketSoporteDTO registrarTicket(TicketSoporteDTO ticketSoporteDTO, String usernameReporta);
    TicketSoporteDTO actualizarTicket(Long id, TicketSoporteDTO ticketSoporteDTO);
    TicketSoporteDTO asignarTicket(Long ticketId, Long responsableId);
    TicketSoporteDTO cambiarEstado(Long ticketId, String estado);
    TicketSoporteDTO obtenerTicketPorId(Long id);
    List<TicketSoporteDTO> listarTodos();
    List<TicketSoporteDTO> listarPorEstado(String estado);
    void eliminarTicket(Long id);

    // --- Gestión de Comentarios ---
    ComentarioTicketDTO agregarComentario(Long ticketId, ComentarioTicketDTO comentarioDTO, String usernameComenta);
    List<ComentarioTicketDTO> listarComentariosPorTicket(Long ticketId);
}