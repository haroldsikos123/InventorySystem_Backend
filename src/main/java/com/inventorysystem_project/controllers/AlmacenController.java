package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.AlmacenDTO;
import com.inventorysystem_project.entities.Almacen;
import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.serviceinterfaces.IAlmacenService;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
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
@RequestMapping("/almacenes")
public class AlmacenController {

    @Autowired
    private IAlmacenService almacenService;

    @Autowired
    private IEmpresaService empresaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody AlmacenDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            Almacen almacen = m.map(dto, Almacen.class);
            
            // Asegurar que el ID sea null para nuevos almacenes
            almacen.setId(null);

            // Establecer la empresa manualmente ya que el DTO solo tiene el ID
            if (dto.getEmpresaId() != null) {
                Empresa empresa = empresaService.listId(dto.getEmpresaId());
                if (empresa != null) {
                    almacen.setEmpresa(empresa);
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "La empresa especificada no existe"));
                }
            }

            almacenService.insert(almacen);
            return ResponseEntity.ok(Map.of("mensaje", "Almacén registrado exitosamente"));
        } catch (DataIntegrityViolationException e) {
            String mensaje = "Error de integridad de datos: ";
            if (e.getMessage().contains("llave duplicada")) {
                mensaje += "Ya existe un almacén con esos datos.";
            } else {
                mensaje += "Los datos no cumplen con las restricciones.";
            }
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar el almacén: " + e.getMessage()));
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<AlmacenDTO> listar() {
        return almacenService.list().stream().map(almacen -> {
            AlmacenDTO dto = new AlmacenDTO();
            dto.setId(almacen.getId());
            if (almacen.getEmpresa() != null) {
                dto.setEmpresaId(almacen.getEmpresa().getId());
            }
            dto.setNombre(almacen.getNombre());
            dto.setUbicacion(almacen.getUbicacion());
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public AlmacenDTO listarPorId(@PathVariable("id") Long id) {
        Almacen almacen = almacenService.listId(id);
        AlmacenDTO dto = new AlmacenDTO();
        dto.setId(almacen.getId());
        if (almacen.getEmpresa() != null) {
            dto.setEmpresaId(almacen.getEmpresa().getId());
        }
        dto.setNombre(almacen.getNombre());
        dto.setUbicacion(almacen.getUbicacion());
        return dto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            boolean tieneMovimientos = almacenService.tieneMovimientosRegistrados(id);
            
            if (tieneMovimientos) {
                long cantidadMovimientos = almacenService.contarMovimientos(id);
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", 
                        "No se puede eliminar el almacén porque tiene " + 
                        cantidadMovimientos + " movimiento(s) registrado(s)."));
            }
            
            almacenService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Almacén eliminado correctamente"));
            
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error al eliminar el almacén: " + e.getMessage()));
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody AlmacenDTO dto) {
        ModelMapper m = new ModelMapper();
        Almacen almacen = m.map(dto, Almacen.class);

        // Establecer la empresa manualmente ya que el DTO solo tiene el ID
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                almacen.setEmpresa(empresa);
            }
        }

        almacenService.insert(almacen);
    }
}
