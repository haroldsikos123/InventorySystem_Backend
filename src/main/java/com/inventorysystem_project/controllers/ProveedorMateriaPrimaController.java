package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProveedorMateriaPrimaDTO;
import com.inventorysystem_project.entities.ProveedorMateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IProveedorMateriaPrimaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveedor-materia-prima")
public class ProveedorMateriaPrimaController {

    @Autowired
    private IProveedorMateriaPrimaService proveedorMateriaPrimaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody ProveedorMateriaPrimaDTO dto) {
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            ProveedorMateriaPrima x = m.map(dto, ProveedorMateriaPrima.class);
            proveedorMateriaPrimaService.insert(x);
            
            // Mapear la respuesta con el ID generado
            ProveedorMateriaPrimaDTO responseDTO = m.map(x, ProveedorMateriaPrimaDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Error de integridad de datos: " + ex.getMostSpecificCause().getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + ex.getMessage()));
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ProveedorMateriaPrimaDTO> listar() {
        return proveedorMateriaPrimaService.list().stream().map(x -> {
            ModelMapper m = new ModelMapper();
            return m.map(x, ProveedorMateriaPrimaDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ProveedorMateriaPrimaDTO listarPorId(@PathVariable("id") Long id) {
        ProveedorMateriaPrima x = proveedorMateriaPrimaService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(x, ProveedorMateriaPrimaDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        proveedorMateriaPrimaService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ProveedorMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        ProveedorMateriaPrima x = m.map(dto, ProveedorMateriaPrima.class);
        proveedorMateriaPrimaService.insert(x);
    }
}
