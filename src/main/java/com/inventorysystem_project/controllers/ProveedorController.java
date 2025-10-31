package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProveedorDTO;
import com.inventorysystem_project.entities.Proveedor;
import com.inventorysystem_project.exceptions.DataIntegrityException;
import com.inventorysystem_project.serviceinterfaces.IProveedorService;
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
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService proveedorService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody ProveedorDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            Proveedor proveedor = m.map(dto, Proveedor.class);
            
            // Asegurar que el ID sea null para nuevos proveedores
            proveedor.setId(null);
            
            proveedorService.insert(proveedor);
            return ResponseEntity.ok(Map.of("mensaje", "Proveedor registrado exitosamente"));
        } catch (DataIntegrityViolationException e) {
            String mensaje = "Error de integridad de datos: ";
            if (e.getMessage().contains("llave duplicada")) {
                mensaje += "Ya existe un proveedor con esos datos.";
            } else {
                mensaje += "Los datos no cumplen con las restricciones.";
            }
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar el proveedor: " + e.getMessage()));
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ProveedorDTO> listar() {
        return proveedorService.list().stream().map(proveedor -> {
            ModelMapper m = new ModelMapper();
            return m.map(proveedor, ProveedorDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ProveedorDTO listarPorId(@PathVariable("id") Long id) {
        Proveedor proveedor = proveedorService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(proveedor, ProveedorDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            proveedorService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error de integridad de datos", "message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error", "message", e.getMessage()));
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ProveedorDTO dto) {
        ModelMapper m = new ModelMapper();
        Proveedor proveedor = m.map(dto, Proveedor.class);
        proveedorService.insert(proveedor);
    }
}
