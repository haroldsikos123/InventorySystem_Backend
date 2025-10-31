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
    
    /**
     * Lista los tickets reportados por un usuario específico, opcionalmente filtrados por estado.
     */
    List<TicketSoporteDTO> listarTicketsPorUsuario(Long usuarioId, String estado);
    
    /**
     * Permite al usuario que reportó el ticket asignarle una calificación.
     * @param ticketId El ID del ticket a calificar.
     * @param calificacion La calificación (1-5).
     * @param username El username del usuario que está calificando (para validación).
     * @return El DTO del ticket actualizado con la calificación.
     */
    TicketSoporteDTO calificarTicket(Long ticketId, Integer calificacion, String username);
    
    /**
     * Obtiene las actividades combinadas (actividades + comentarios) de un ticket específico.
     * @param ticketId El ID del ticket.
     * @return Lista de actividades combinadas ordenadas por fecha descendente.
     */
    List<com.inventorysystem_project.dtos.ActividadCombinadaDTO> getActividadesCombinadas(Long ticketId);
    
    /**
     * Actualiza solo la descripción y solución de un ticket (para usuarios limitados).
     * @param ticketId El ID del ticket.
     * @param nuevaDescripcion La nueva descripción.
     * @param nuevaSolucion La nueva solución (puede ser null).
     * @return El DTO del ticket actualizado.
     */
    TicketSoporteDTO actualizarDescripcionYSolucion(Long ticketId, String nuevaDescripcion, String nuevaSolucion);
}