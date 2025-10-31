package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProveedorDTO;
import com.inventorysystem_project.entities.Proveedor;
import com.inventorysystem_project.exceptions.DataIntegrityException;
import com.inventorysystem_project.services.RetryService;
import com.inventorysystem_project.serviceinterfaces.IProveedorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService proveedorService;
    
    @Autowired
    private RetryService retryService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody ProveedorDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            Proveedor proveedor = m.map(dto, Proveedor.class);
            
            // Asegurar que el ID sea null para nuevos proveedores
            proveedor.setId(null);
            
            // Usar RetryService para manejar secuencias desincronizadas automáticamente
            retryService.executeWithRetry(() -> {
                proveedorService.insert(proveedor);
                return proveedor;
            }, "Proveedor");
            
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
    public ResponseEntity<?> listarPorId(@PathVariable("id") Long id) {
        System.out.println("🔍 Request GET proveedor con ID: " + id);
        
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de proveedor inválido: " + id));
            }
            
            Proveedor proveedor = proveedorService.listId(id);
            if (proveedor == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El proveedor con ID " + id + " no existe"));
            }
            
            ModelMapper m = new ModelMapper();
            ProveedorDTO dto = m.map(proveedor, ProveedorDTO.class);
            
            System.out.println("✅ Proveedor encontrado: " + proveedor.getNombreEmpresaProveedor());
            System.out.println("🌍 País: " + proveedor.getPais());
            
            return ResponseEntity.ok(dto);
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error obteniendo proveedor: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al obtener el proveedor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        System.out.println("🔍 Request DELETE proveedor con ID: " + id);
        
        try {
            // Validar ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de proveedor inválido: " + id));
            }
            
            // Verificar si el proveedor existe
            Proveedor proveedor = proveedorService.listId(id);
            if (proveedor == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El proveedor con ID " + id + " no existe"));
            }
            
            System.out.println("✅ Proveedor encontrado: " + proveedor.getNombreEmpresaProveedor());
            
            // Usar RetryService para manejar posibles conflictos de eliminación
            retryService.executeWithRetry(() -> {
                proveedorService.delete(id);
                return true;
            }, "Eliminación de Proveedor");
            
            return ResponseEntity.ok(Map.of("mensaje", "Proveedor eliminado exitosamente"));
            
        } catch (DataIntegrityViolationException e) {
            String mensaje = "No se puede eliminar el proveedor: ";
            String errorDetail = e.getMessage().toLowerCase();
            
            if (errorDetail.contains("orden_compra") || errorDetail.contains("foreign key")) {
                mensaje += "Tiene órdenes de compra asociadas. Debe eliminar primero todas las órdenes de compra relacionadas.";
            } else if (errorDetail.contains("proveedor_materia_prima")) {
                mensaje += "Tiene materias primas asociadas. Debe desasociar primero las materias primas relacionadas.";
            } else {
                mensaje += "Está siendo referenciado por otros registros en el sistema.";
            }
            
            System.out.println("❌ Error integridad: " + mensaje);
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
            
        } catch (DataIntegrityException e) {
            System.out.println("❌ DataIntegrityException: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
                
        } catch (NumberFormatException e) {
            System.out.println("❌ NumberFormatException: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "ID de proveedor inválido: debe ser un número válido"));
                
        } catch (RuntimeException e) {
            String errorMsg = e.getMessage();
            System.out.println("❌ RuntimeException: " + errorMsg);
            
            if (errorMsg != null && (errorMsg.contains("no encontrado") || errorMsg.contains("not found"))) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El proveedor con ID " + id + " no existe"));
            } else {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al eliminar el proveedor: " + errorMsg));
            }
        } catch (Exception e) {
            System.out.println("❌ Exception general: " + e.getMessage());
            return ResponseEntity.status(500)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @RequestBody ProveedorDTO dto) {
        System.out.println("🔄 Request PUT proveedor con ID: " + id);
        System.out.println("📝 Datos recibidos: " + dto.getNombreEmpresaProveedor());
        
        try {
            // Validar que el ID no sea null
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de proveedor inválido: " + id));
            }
            
            // Verificar que el proveedor existe
            Proveedor proveedorExistente = proveedorService.listId(id);
            if (proveedorExistente == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El proveedor con ID " + id + " no existe"));
            }
            
            // Mapear DTO a entidad y asegurar que el ID se mantenga
            ModelMapper m = new ModelMapper();
            Proveedor proveedor = m.map(dto, Proveedor.class);
            proveedor.setId(id); // CRÍTICO: Mantener el ID para UPDATE
            
            System.out.println("✅ Actualizando proveedor existente: " + proveedor.getNombreEmpresaProveedor() + " (ID: " + id + ")");
            
            // Usar RetryService para la actualización
            retryService.executeWithRetry(() -> {
                proveedorService.insert(proveedor); // insert hace UPDATE si el ID existe
                return proveedor;
            }, "Actualización de Proveedor");
            
            return ResponseEntity.ok(Map.of("mensaje", "Proveedor actualizado exitosamente"));
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error actualizando proveedor: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al actualizar el proveedor: " + e.getMessage()));
        }
    }
    
    @GetMapping("/paises")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> listarPaises() {
        // Lista de países comunes para el select
        List<String> paises = Arrays.asList(
            "Perú", "Argentina", "Brasil", "Chile", "Colombia", "Ecuador", 
            "Bolivia", "Paraguay", "Uruguay", "Venezuela", "México", 
            "Estados Unidos", "Canadá", "España", "Francia", "Italia", 
            "Alemania", "Reino Unido", "China", "Japón", "Corea del Sur", "Otro"
        );
        
        return ResponseEntity.ok(paises);
    }
}
