package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ActividadCombinadaDTO;
import com.inventorysystem_project.dtos.TicketSoporteDTO;
import com.inventorysystem_project.serviceinterfaces.ITicketSoporteService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller específico para endpoints de ticket soporte con ruta /ticketsoporte
 * Mantiene compatibilidad con el frontend existente
 */
@RestController
@RequestMapping("/api/soporte/ticketsoporte")
public class TicketSoporteCompatibilityController {

    @Autowired
    private ITicketSoporteService ticketService;

    /**
     * Endpoint para actualizar un ticket (compatibilidad con frontend)
     * PUT /api/soporte/ticketsoporte/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2') or hasAuthority('GESTOR_CAMBIOS') or hasAuthority('CAB_MEMBER') or hasAuthority('PROJECT_MANAGER')")
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

    /**
     * Endpoint para obtener las actividades combinadas (actividades + comentarios) de un ticket.
     * GET /api/soporte/ticketsoporte/{id}/actividades
     */
    @GetMapping("/{id}/actividades")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ActividadCombinadaDTO>> obtenerActividadesCombinadas(@PathVariable("id") Long ticketId) {
        try {
            List<ActividadCombinadaDTO> actividades = ticketService.getActividadesCombinadas(ticketId);
            return ResponseEntity.ok(actividades);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado", e);
        } catch (Exception e) {
            System.err.println("Error al obtener actividades del ticket ID " + ticketId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las actividades del ticket", e);
        }
    }
}