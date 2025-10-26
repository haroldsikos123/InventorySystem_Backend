package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.SolicitudCambioDTO;
import com.inventorysystem_project.serviceinterfaces.IGestionCambiosService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/soporte/cambios")
public class GestionCambiosController {

    @Autowired
    private IGestionCambiosService cambiosService;

    @PostMapping("/rfc")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolicitudCambioDTO> registrarRFC(@RequestBody SolicitudCambioDTO solicitudCambioDTO) {
        try {
            if (solicitudCambioDTO.getSolicitanteId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del solicitante es obligatorio.");
            }
            if (solicitudCambioDTO.getTitulo() == null || solicitudCambioDTO.getTitulo().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio.");
            }
            if (solicitudCambioDTO.getDescripcion() == null || solicitudCambioDTO.getDescripcion().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria.");
            }
            if (solicitudCambioDTO.getJustificacion() == null || solicitudCambioDTO.getJustificacion().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La justificación es obligatoria.");
            }
            if (solicitudCambioDTO.getTipoCambio() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de cambio es obligatorio.");
            }

            SolicitudCambioDTO nuevaRFC = cambiosService.registrarRFC(solicitudCambioDTO);
            return new ResponseEntity<>(nuevaRFC, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al registrar RFC: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar la solicitud de cambio.", e);
        }
    }

    @GetMapping("/rfc")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SolicitudCambioDTO>> listarRFCs(@RequestParam(required = false) String tipo) {
        try {
            List<SolicitudCambioDTO> rfcs;
            if (tipo != null && !tipo.trim().isEmpty()) {
                rfcs = cambiosService.listarRFCsPorTipo(tipo);
            } else {
                rfcs = cambiosService.listarRFCs();
            }
            return new ResponseEntity<>(rfcs, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al listar RFCs: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar las solicitudes de cambio.", e);
        }
    }

    @GetMapping("/rfc/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolicitudCambioDTO> obtenerRFCPorId(@PathVariable Long id) {
        try {
            SolicitudCambioDTO rfc = cambiosService.obtenerRFCPorId(id);
            return new ResponseEntity<>(rfc, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al obtener RFC ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener la solicitud de cambio.", e);
        }
    }

    @PutMapping("/rfc/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GESTOR_CAMBIOS') or @gestionCambiosServiceImplement.obtenerRFCPorId(#id).solicitanteId == principal.id")
    public ResponseEntity<SolicitudCambioDTO> actualizarRFC(@PathVariable Long id, @RequestBody SolicitudCambioDTO solicitudCambioDTO) {
        try {
            if (solicitudCambioDTO.getTitulo() == null || solicitudCambioDTO.getTitulo().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio.");
            }

            SolicitudCambioDTO rfcActualizada = cambiosService.actualizarRFC(id, solicitudCambioDTO);
            return new ResponseEntity<>(rfcActualizada, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al actualizar RFC ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar la solicitud de cambio.", e);
        }
    }

    @PutMapping("/rfc/{rfcId}/aprobar")
    @PreAuthorize("hasAuthority('CAB_MEMBER') or hasAuthority('PROJECT_MANAGER')")
    public ResponseEntity<SolicitudCambioDTO> aprobarRFC(@PathVariable Long rfcId, @RequestBody Map<String, Object> payload) {
        Long aprobadorId = null;
        try {
            aprobadorId = Long.valueOf(payload.get("aprobadorId").toString());
        } catch (NumberFormatException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere 'aprobadorId' (numérico) en el body.");
        }
        String tipoAprobacion = (String) payload.get("tipoAprobacion");

        if (tipoAprobacion == null || tipoAprobacion.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere 'tipoAprobacion' ('CAB' o 'PM') en el body.");
        }

        try {
            SolicitudCambioDTO rfcAprobada = cambiosService.aprobarCambio(rfcId, aprobadorId, tipoAprobacion);
            return new ResponseEntity<>(rfcAprobada, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al aprobar RFC ID " + rfcId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al aprobar la solicitud de cambio.", e);
        }
    }

    @PutMapping("/rfc/{rfcId}/estado")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GESTOR_CAMBIOS')")
    public ResponseEntity<SolicitudCambioDTO> cambiarEstadoRFC(@PathVariable Long rfcId, @RequestBody Map<String, String> payload) {
        String nuevoEstado = payload.get("estado");
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'estado' es requerido en el body.");
        }
        try {
            SolicitudCambioDTO rfcActualizada = cambiosService.cambiarEstadoRFC(rfcId, nuevoEstado);
            return new ResponseEntity<>(rfcActualizada, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al cambiar estado de RFC ID " + rfcId + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cambiar el estado de la solicitud de cambio.", e);
        }
    }
}