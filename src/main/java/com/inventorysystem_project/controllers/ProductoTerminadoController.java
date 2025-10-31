package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProductoTerminadoDTO;
import com.inventorysystem_project.entities.ProductoTerminado;
import com.inventorysystem_project.serviceinterfaces.IProductoTerminadoService;
import com.inventorysystem_project.services.RetryService;
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

    @Autowired
    private RetryService retryService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody ProductoTerminadoDTO dto) {
        System.out.println("🔍 Request POST producto terminado: " + dto.toString());
        
        try {
            ModelMapper m = new ModelMapper();
            ProductoTerminado productoTerminado = m.map(dto, ProductoTerminado.class);
            
            // Asegurar que el ID sea null para nuevos productos
            productoTerminado.setId(null);
            
            // Usar RetryService para manejar desincronización de secuencias
            retryService.executeWithRetry(() -> {
                productoTerminadoService.insert(productoTerminado);
                return true;
            }, "Registro de Producto Terminado");
            
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
    public ResponseEntity<?> listarPorId(@PathVariable("id") Long id) {
        System.out.println("🔍 Request GET producto terminado con ID: " + id);
        
        try {
            // Validar ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de producto terminado inválido: " + id));
            }
            
            ProductoTerminado productoTerminado = productoTerminadoService.listId(id);
            
            if (productoTerminado == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El producto terminado con ID " + id + " no existe"));
            }
            
            ModelMapper m = new ModelMapper();
            ProductoTerminadoDTO dto = m.map(productoTerminado, ProductoTerminadoDTO.class);
            
            System.out.println("✅ Producto terminado encontrado: " + dto.toString());
            return ResponseEntity.ok(dto);
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error al buscar producto terminado: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al buscar el producto terminado: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        System.out.println("🔍 Request DELETE producto terminado con ID: " + id);
        
        try {
            // Validar ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de producto terminado inválido: " + id));
            }
            
            // Verificar si el producto existe
            ProductoTerminado productoTerminado = productoTerminadoService.listId(id);
            if (productoTerminado == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El producto terminado con ID " + id + " no existe"));
            }
            
            // Usar RetryService para manejar posibles conflictos de eliminación
            retryService.executeWithRetry(() -> {
                productoTerminadoService.delete(id);
                return true;
            }, "Eliminación de Producto Terminado");
            
            return ResponseEntity.ok(Map.of("mensaje", "Producto terminado eliminado exitosamente"));
            
        } catch (DataIntegrityViolationException e) {
            String mensaje = "No se puede eliminar el producto terminado: Está siendo referenciado por otros registros en el sistema.";
            System.out.println("❌ Error integridad: " + mensaje);
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
            
        } catch (RuntimeException e) {
            System.out.println("❌ RuntimeException: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al eliminar el producto terminado: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @RequestBody ProductoTerminadoDTO dto) {
        System.out.println("🔍 Request PUT producto terminado con ID: " + id + " y datos: " + dto.toString());
        
        try {
            // Validar ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de producto terminado inválido: " + id));
            }
            
            // Verificar si el producto existe
            ProductoTerminado existente = productoTerminadoService.listId(id);
            if (existente == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El producto terminado con ID " + id + " no existe"));
            }
            
            ModelMapper m = new ModelMapper();
            ProductoTerminado productoTerminado = m.map(dto, ProductoTerminado.class);
            
            // ⚠️ CRÍTICO: Preservar el ID para que sea una actualización, no una inserción
            productoTerminado.setId(id);
            
            System.out.println("✅ Producto terminado a actualizar - ID: " + productoTerminado.getId());
            
            // Usar RetryService para manejar posibles conflictos
            retryService.executeWithRetry(() -> {
                productoTerminadoService.insert(productoTerminado);
                return true;
            }, "Actualización de Producto Terminado");
            
            return ResponseEntity.ok(Map.of("mensaje", "Producto terminado actualizado exitosamente"));
            
        } catch (RuntimeException e) {
            System.out.println("❌ RuntimeException: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al modificar el producto terminado: " + e.getMessage()));
        }
    }
}
