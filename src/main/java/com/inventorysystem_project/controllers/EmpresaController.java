package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.EmpresaDTO;
import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
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
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService empresaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody EmpresaDTO dto) {
        try {
            ModelMapper m = new ModelMapper();
            Empresa empresa = m.map(dto, Empresa.class);
            
            // Asegurar que el ID sea null para nuevas empresas
            empresa.setId(null);
            
            empresaService.insert(empresa);
            return ResponseEntity.ok(Map.of("mensaje", "Empresa registrada exitosamente"));
        } catch (DataIntegrityViolationException e) {
            String mensaje = "Error de integridad de datos: ";
            if (e.getMessage().contains("llave duplicada")) {
                mensaje += "Ya existe una empresa con esos datos.";
            } else {
                mensaje += "Los datos no cumplen con las restricciones.";
            }
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar la empresa: " + e.getMessage()));
        }
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<EmpresaDTO> listar() {
        return empresaService.list().stream().map(empresa -> {
            ModelMapper m = new ModelMapper();
            return m.map(empresa, EmpresaDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public EmpresaDTO listarPorId(@PathVariable("id") Long id) {
        Empresa empresa = empresaService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(empresa, EmpresaDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        empresaService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody EmpresaDTO dto) {
        ModelMapper m = new ModelMapper();
        Empresa empresa = m.map(dto, Empresa.class);
        empresaService.insert(empresa);
    }
}