package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.MateriaPrimaDTO;
import com.inventorysystem_project.entities.MateriaPrima;
import com.inventorysystem_project.services.RetryService;
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
    
    @Autowired
    private RetryService retryService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody MateriaPrimaDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            MateriaPrima materiaPrima = m.map(dto, MateriaPrima.class);
            
            // Asegurar que el ID sea null para nueva materia prima
            materiaPrima.setId(null);
            
            // Usar RetryService para manejar secuencias desincronizadas automáticamente
            retryService.executeWithRetry(() -> {
                materiaPrimaService.insert(materiaPrima);
                return materiaPrima;
            }, "Materia Prima");
            
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
    public ResponseEntity<?> listarPorId(@PathVariable("id") Long id) {
        System.out.println("🔍 Request GET materia prima con ID: " + id);
        
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de materia prima inválido: " + id));
            }
            
            MateriaPrima materiaPrima = materiaPrimaService.listId(id);
            if (materiaPrima == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La materia prima con ID " + id + " no existe"));
            }
            
            ModelMapper m = new ModelMapper();
            MateriaPrimaDTO dto = m.map(materiaPrima, MateriaPrimaDTO.class);
            
            System.out.println("✅ Materia prima encontrada: " + materiaPrima.getNombre());
            
            return ResponseEntity.ok(dto);
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error obteniendo materia prima: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al obtener la materia prima: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        System.out.println("🔍 Request DELETE materia prima con ID: " + id);
        
        try {
            // Validar ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de materia prima inválido: " + id));
            }
            
            // Verificar si la materia prima existe
            MateriaPrima materiaPrima = materiaPrimaService.listId(id);
            if (materiaPrima == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La materia prima con ID " + id + " no existe"));
            }
            
            // Usar RetryService para manejar posibles conflictos de eliminación
            retryService.executeWithRetry(() -> {
                materiaPrimaService.delete(id);
                return true;
            }, "Eliminación de Materia Prima");
            
            return ResponseEntity.ok(Map.of("mensaje", "Materia prima eliminada exitosamente"));
            
        } catch (DataIntegrityViolationException e) {
            String mensaje = "No se puede eliminar la materia prima: Está siendo referenciada por otros registros en el sistema.";
            System.out.println("❌ Error integridad: " + mensaje);
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
            
        } catch (RuntimeException e) {
            System.out.println("❌ RuntimeException: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al eliminar la materia prima: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @RequestBody MateriaPrimaDTO dto) {
        System.out.println("🔄 Request PUT materia prima con ID: " + id);
        System.out.println("📝 Datos recibidos: " + dto.getNombre());
        
        try {
            // Validar que el ID no sea null
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de materia prima inválido: " + id));
            }
            
            // Verificar que la materia prima existe
            MateriaPrima materiaPrimaExistente = materiaPrimaService.listId(id);
            if (materiaPrimaExistente == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "La materia prima con ID " + id + " no existe"));
            }
            
            // Mapear DTO a entidad y asegurar que el ID se mantenga
            ModelMapper m = new ModelMapper();
            MateriaPrima materiaPrima = m.map(dto, MateriaPrima.class);
            materiaPrima.setId(id); // CRÍTICO: Mantener el ID para UPDATE
            
            System.out.println("✅ Actualizando materia prima existente: " + materiaPrima.getNombre() + " (ID: " + id + ")");
            
            // Usar RetryService para la actualización
            retryService.executeWithRetry(() -> {
                materiaPrimaService.insert(materiaPrima); // insert hace UPDATE si el ID existe
                return materiaPrima;
            }, "Actualización de Materia Prima");
            
            return ResponseEntity.ok(Map.of("mensaje", "Materia prima actualizada exitosamente"));
            
        } catch (RuntimeException e) {
            System.out.println("❌ Error actualizando materia prima: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al actualizar la materia prima: " + e.getMessage()));
        }
    }
}
