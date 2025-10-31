package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.MateriaPrimaDTO;
import com.inventorysystem_project.entities.MateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IMateriaPrimaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/materia-prima")
public class MateriaPrimaController {

    @Autowired
    private IMateriaPrimaService materiaPrimaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody MateriaPrimaDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            MateriaPrima materiaPrima = m.map(dto, MateriaPrima.class);
            
            // Asegurar que el ID sea null para nueva materia prima
            materiaPrima.setId(null);
            
            materiaPrimaService.insert(materiaPrima);
            return ResponseEntity.ok(Map.of("mensaje", "Materia prima registrada exitosamente"));
        } catch (DataIntegrityViolationException e) {
            String mensaje = "Error de integridad de datos: ";
            if (e.getMessage().contains("llave duplicada")) {
                mensaje += "Ya existe una materia prima con esos datos.";
            } else {
                mensaje += "Los datos no cumplen con las restricciones.";
            }
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar la materia prima: " + e.getMessage()));
        }
    }

    @GetMapping("listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<MateriaPrimaDTO> listar() {
        return materiaPrimaService.list().stream().map(materiaPrima -> {
            ModelMapper m = new ModelMapper();
            return m.map(materiaPrima, MateriaPrimaDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public MateriaPrimaDTO listarPorId(@PathVariable("id") Long id) {
        MateriaPrima materiaPrima = materiaPrimaService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(materiaPrima, MateriaPrimaDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        materiaPrimaService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody MateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        MateriaPrima materiaPrima = m.map(dto, MateriaPrima.class);
        materiaPrimaService.insert(materiaPrima);
    }
}
