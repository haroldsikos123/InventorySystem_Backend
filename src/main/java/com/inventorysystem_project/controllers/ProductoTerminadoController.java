package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProductoTerminadoDTO;
import com.inventorysystem_project.entities.ProductoTerminado;
import com.inventorysystem_project.serviceinterfaces.IProductoTerminadoService;
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
@RequestMapping("/productos-terminados")
public class ProductoTerminadoController {

    @Autowired
    private IProductoTerminadoService productoTerminadoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody ProductoTerminadoDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            ProductoTerminado productoTerminado = m.map(dto, ProductoTerminado.class);
            
            // Asegurar que el ID sea null para nuevos productos
            productoTerminado.setId(null);
            
            productoTerminadoService.insert(productoTerminado);
            
            return ResponseEntity.ok(Map.of("mensaje", "Producto terminado registrado exitosamente"));
        } catch (DataIntegrityViolationException e) {
            String mensaje = "Error de integridad de datos: ";
            if (e.getMessage().contains("llave duplicada")) {
                mensaje += "Ya existe un producto con esos datos. Verifique que no sea un duplicado.";
            } else if (e.getMessage().contains("constraint")) {
                mensaje += "Los datos no cumplen con las restricciones de la base de datos.";
            } else {
                mensaje += "Error en la validación de datos.";
            }
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar el producto: " + e.getMessage()));
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ProductoTerminadoDTO> listar() {
        return productoTerminadoService.list().stream().map(productoTerminado -> {
            ModelMapper m = new ModelMapper();
            return m.map(productoTerminado, ProductoTerminadoDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ProductoTerminadoDTO listarPorId(@PathVariable("id") Long id) {
        ProductoTerminado productoTerminado = productoTerminadoService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(productoTerminado, ProductoTerminadoDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        productoTerminadoService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ProductoTerminadoDTO dto) {
        ModelMapper m = new ModelMapper();
        ProductoTerminado productoTerminado = m.map(dto, ProductoTerminado.class);
        productoTerminadoService.insert(productoTerminado);
    }
}
