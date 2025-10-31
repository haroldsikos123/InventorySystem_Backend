package com.inventorysystem_project.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventorysystem_project.dtos.RolDTO;
import com.inventorysystem_project.entities.Rol;
import com.inventorysystem_project.serviceinterfaces.IRolService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RolController {
    @Autowired
    private IRolService rolR;

    @PostMapping("/registrar")
    // @PreAuthorize("hasAuthority('ADMIN')") // <-- ELIMINA O COMENTA ESTA LÍNEA
    public ResponseEntity<?> registrar(@RequestBody RolDTO dto){
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            Rol d = m.map(dto, Rol.class);
            rolR.insert(d);
            
            // Mapear la respuesta con el ID generado
            RolDTO responseDTO = m.map(d, RolDTO.class);
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
    // @PreAuthorize("hasAuthority('ADMIN')") // <-- ELIMINA O COMENTA ESTA LÍNEA
    public List<RolDTO> listar(){
        return rolR.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,RolDTO.class);
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAuthority('ADMIN')") // <-- DEJA ESTA (Eliminar debe ser solo para Admin)
    public void eliminar(@PathVariable("id")Long id){
        rolR.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')") // <-- DEJA ESTA (Modificar debe ser solo para Admin)
    public void modificar(@RequestBody RolDTO dto){
        ModelMapper m=new ModelMapper();
        Rol d=m.map(dto, Rol.class);
        rolR.insert(d);
    }
}

