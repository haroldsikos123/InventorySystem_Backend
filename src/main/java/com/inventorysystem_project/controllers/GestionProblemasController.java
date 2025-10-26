package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ErrorConocidoDTO;
import com.inventorysystem_project.serviceinterfaces.IGestionProblemasService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/soporte/problemas")
public class GestionProblemasController {

    @Autowired
    private IGestionProblemasService problemasService;

    @PostMapping("/errores-conocidos")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N2')")
    public ResponseEntity<ErrorConocidoDTO> registrarErrorConocido(@RequestBody ErrorConocidoDTO errorConocidoDTO) {
         try {
            if (errorConocidoDTO.getDescripcionError() == null || errorConocidoDTO.getDescripcionError().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción del error es obligatoria.");
            }
            ErrorConocidoDTO nuevoError = problemasService.registrarErrorConocido(errorConocidoDTO);
            return new ResponseEntity<>(nuevoError, HttpStatus.CREATED);
         } catch (Exception e) {
            System.err.println("Error al registrar error conocido: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar el error conocido.", e);
         }
    }

    @GetMapping("/errores-conocidos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ErrorConocidoDTO>> listarErroresConocidos() {
        try {
            List<ErrorConocidoDTO> errores = problemasService.listarErroresConocidos();
            return new ResponseEntity<>(errores, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al listar errores conocidos: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al listar los errores conocidos.", e);
        }
    }

    @GetMapping("/errores-conocidos/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ErrorConocidoDTO> obtenerErrorConocidoPorId(@PathVariable Long id) {
        try {
            ErrorConocidoDTO error = problemasService.obtenerErrorPorId(id);
            return new ResponseEntity<>(error, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al obtener error conocido ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el error conocido.", e);
        }
    }

    @PutMapping("/errores-conocidos/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N2')")
    public ResponseEntity<ErrorConocidoDTO> actualizarErrorConocido(@PathVariable Long id, @RequestBody ErrorConocidoDTO errorConocidoDTO) {
        try {
            if (errorConocidoDTO.getDescripcionError() == null || errorConocidoDTO.getDescripcionError().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción del error es obligatoria.");
            }
            if (errorConocidoDTO.getEstado() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado del problema es obligatorio.");
            }
            ErrorConocidoDTO errorActualizado = problemasService.actualizarErrorConocido(id, errorConocidoDTO);
            return new ResponseEntity<>(errorActualizado, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al actualizar error conocido ID " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el error conocido.", e);
        }
    }
}