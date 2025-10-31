package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.MovimientoInventarioMateriaPrimaDTO;
import com.inventorysystem_project.entities.MovimientoInventarioMateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IMovimientoInventarioMateriaPrimaService;
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
@RequestMapping("/movimientos-materia-prima")
public class MovimientoInventarioMateriaPrimaController {

    @Autowired
    private IMovimientoInventarioMateriaPrimaService movimientoService;

    // Registrar un nuevo movimiento
    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody MovimientoInventarioMateriaPrimaDTO dto) {
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            MovimientoInventarioMateriaPrima movimiento = m.map(dto, MovimientoInventarioMateriaPrima.class);
            movimientoService.insert(movimiento);
            
            // Mapear la respuesta con el ID generado
            MovimientoInventarioMateriaPrimaDTO responseDTO = m.map(movimiento, MovimientoInventarioMateriaPrimaDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Error de integridad de datos: " + ex.getMostSpecificCause().getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + ex.getMessage()));
        }
    }

    // Listar todos los movimientos
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<MovimientoInventarioMateriaPrimaDTO> listar() {
        return movimientoService.list().stream().map(movimiento -> {
            ModelMapper m = new ModelMapper();
            return m.map(movimiento, MovimientoInventarioMateriaPrimaDTO.class);
        }).collect(Collectors.toList());
    }

    // Obtener un movimiento por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public MovimientoInventarioMateriaPrimaDTO listarPorId(@PathVariable("id") Long id) {
        MovimientoInventarioMateriaPrima movimiento = movimientoService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(movimiento, MovimientoInventarioMateriaPrimaDTO.class);
    }

    // Eliminar un movimiento
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        movimientoService.delete(id);
    }

    // Modificar un movimiento
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody MovimientoInventarioMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        MovimientoInventarioMateriaPrima movimiento = m.map(dto, MovimientoInventarioMateriaPrima.class);
        movimientoService.insert(movimiento);
    }
}
