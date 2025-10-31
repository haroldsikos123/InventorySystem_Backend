package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ComentarioTicketDTO;
import com.inventorysystem_project.dtos.TicketSoporteDTO;
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.serviceinterfaces.ITicketSoporteService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/soporte/tickets")
public class TicketSoporteController {

    @Autowired
    private ITicketSoporteService ticketService;

    @Autowired
    private com.inventorysystem_project.serviceinterfaces.IUsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketSoporteDTO> registrarTicket(
            @RequestBody TicketSoporteDTO ticketSoporteDTO,
            Authentication authentication) {
         try {
            if (ticketSoporteDTO.getDescripcion() == null || ticketSoporteDTO.getDescripcion().trim().isEmpty()) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción del ticket es obligatoria.");
            }
            if (ticketSoporteDTO.getPrioridad() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La prioridad del ticket es obligatoria.");
            }
            if (ticketSoporteDTO.getTipo() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de ticket (INCIDENTE/PROBLEMA) es obligatorio.");
            }

            String username = authentication.getName();
            TicketSoporteDTO nuevoTicket = ticketService.registrarTicket(ticketSoporteDTO, username);
            return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al registrar ticket: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al registrar el ticket.", e);
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TicketSoporteDTO>> listarTickets(@RequestParam(required = false) String estado) {
        try {
            List<TicketSoporteDTO> tickets;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Verificar si el usuario tiene rol restringido (USER o GUEST)
            boolean esUsuarioRestringido = authentication.getAuthorities().size() == 1 &&
                             authentication.getAuthorities().stream()
                                 .anyMatch(a -> a.getAuthority().equals("USER") || 
                                              a.getAuthority().equals("GUEST"));

            if (esUsuarioRestringido) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                Usuario usuarioActual = usuarioService.findByUsername(userDetails.getUsername());
                if (usuarioActual == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario actual no encontrado.");
                }
                tickets = ticketService.listarTicketsPorUsuario(usuarioActual.getId(), estado);
            } else {
                if (estado != null && !estado.trim().isEmpty()) {
                    tickets = ticketService.listarPorEstado(estado);
                } else {
                    tickets = ticketService.listarTodos();
                }
            }
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al listar tickets: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al listar los tickets.", e);
        }
    }    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and (hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2') or @ticketSoporteServiceImplement.obtenerTicketPorId(#id).usuarioReportaUsername == principal.username)")
    public ResponseEntity<TicketSoporteDTO> obtenerTicketPorId(@PathVariable Long id) {
        try {
            TicketSoporteDTO ticket = ticketService.obtenerTicketPorId(id);
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al obtener ticket ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al obtener el ticket.", e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2')")
    public ResponseEntity<TicketSoporteDTO> actualizarTicket(@PathVariable Long id, @RequestBody TicketSoporteDTO ticketSoporteDTO) {
         try {
            if (ticketSoporteDTO.getDescripcion() == null || ticketSoporteDTO.getDescripcion().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción del ticket es obligatoria.");
            }

            TicketSoporteDTO ticketActualizado = ticketService.actualizarTicket(id, ticketSoporteDTO);
            return new ResponseEntity<>(ticketActualizado, HttpStatus.OK);
         } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
         } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
         } catch (Exception e) {
            System.err.println("Error al actualizar ticket ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al actualizar el ticket.", e);
         }
    }

    @PutMapping("/{ticketId}/asignar/{responsableId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1')")
    public ResponseEntity<TicketSoporteDTO> asignarTicket(@PathVariable Long ticketId, @PathVariable Long responsableId) {
        try {
            TicketSoporteDTO ticketAsignado = ticketService.asignarTicket(ticketId, responsableId);
            return new ResponseEntity<>(ticketAsignado, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al asignar ticket ID " + ticketId + " a usuario " + responsableId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al asignar el ticket.", e);
        }
    }

    @PutMapping("/{ticketId}/estado")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2') or @ticketSoporteServiceImplement.obtenerTicketPorId(#ticketId).responsableAsignadoId == principal.id")
    public ResponseEntity<TicketSoporteDTO> cambiarEstadoTicket(@PathVariable Long ticketId, @RequestBody Map<String, String> payload) {
        String nuevoEstado = payload.get("estado");
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'estado' es requerido en el cuerpo de la solicitud (body).");
        }
        try {
            TicketSoporteDTO ticketActualizado = ticketService.cambiarEstado(ticketId, nuevoEstado);
            return new ResponseEntity<>(ticketActualizado, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al cambiar estado del ticket ID " + ticketId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al cambiar el estado del ticket.", e);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        try {
            ticketService.eliminarTicket(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al eliminar ticket ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al eliminar el ticket.", e);
        }
    }

    @PostMapping("/{ticketId}/comentarios")
    @PreAuthorize("isAuthenticated() and (hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2') or @ticketSoporteServiceImplement.obtenerTicketPorId(#ticketId).usuarioReportaUsername == principal.username)")
    public ResponseEntity<ComentarioTicketDTO> agregarComentario(
            @PathVariable Long ticketId, 
            @RequestBody ComentarioTicketDTO comentarioDTO,
            Authentication authentication) {
        try {
            if (comentarioDTO.getTexto() == null || comentarioDTO.getTexto().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El texto del comentario es obligatorio.");
            }
            String username = authentication.getName();
            ComentarioTicketDTO nuevoComentario = ticketService.agregarComentario(ticketId, comentarioDTO, username);
            return new ResponseEntity<>(nuevoComentario, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al agregar comentario al ticket ID " + ticketId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al agregar el comentario.", e);
        }
    }

    @GetMapping("/{ticketId}/comentarios")
    @PreAuthorize("isAuthenticated() and (hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2') or @ticketSoporteServiceImplement.obtenerTicketPorId(#ticketId).usuarioReportaUsername == principal.username)")
    public ResponseEntity<List<ComentarioTicketDTO>> listarComentarios(@PathVariable Long ticketId) {
        try {
            List<ComentarioTicketDTO> comentarios = ticketService.listarComentariosPorTicket(ticketId);
            return new ResponseEntity<>(comentarios, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al listar comentarios del ticket ID " + ticketId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al listar los comentarios.", e);
        }
    }

    @PatchMapping("/{ticketId}/calificar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TicketSoporteDTO> calificarTicket(
            @PathVariable Long ticketId,
            @RequestBody Map<String, Integer> payload,
            Authentication authentication) {
        
        Integer calificacion = payload.get("calificacion");
        if (calificacion == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'calificacion' es requerido en el body.");
        }

        try {
            // Usamos el nombre del principal (token) para máxima seguridad
            String username = authentication.getName(); 
            TicketSoporteDTO ticketActualizado = ticketService.calificarTicket(ticketId, calificacion, username);
            return new ResponseEntity<>(ticketActualizado, HttpStatus.OK);
        
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Captura si el ticket no está 'RESUELTO' o la calificación es inválida
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (AccessDeniedException e) { 
            // Captura si el usuario no es el dueño del ticket
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al calificar ticket ID " + ticketId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al calificar el ticket.", e);
        }
    }
}