package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.MovimientoInventarioProductoTerminadoDTO;
import com.inventorysystem_project.entities.MovimientoInventarioProductoTerminado;
import com.inventorysystem_project.serviceinterfaces.IMovimientoInventarioProductoTerminadoService;
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
@RequestMapping("/movimientos-producto-terminado")
public class MovimientoInventarioProductoTerminadoController {

    @Autowired
    private IMovimientoInventarioProductoTerminadoService ventaProductoTerminadoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody MovimientoInventarioProductoTerminadoDTO dto) {
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            MovimientoInventarioProductoTerminado x = m.map(dto, MovimientoInventarioProductoTerminado.class);
            ventaProductoTerminadoService.insert(x);
            
            // Mapear la respuesta con el ID generado
            MovimientoInventarioProductoTerminadoDTO responseDTO = m.map(x, MovimientoInventarioProductoTerminadoDTO.class);
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
    public List<MovimientoInventarioProductoTerminadoDTO> listar() {
        return ventaProductoTerminadoService.list().stream().map(ventaProductoTerminado -> {
            ModelMapper m = new ModelMapper();
            return m.map(ventaProductoTerminado, MovimientoInventarioProductoTerminadoDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public MovimientoInventarioProductoTerminadoDTO listarPorId(@PathVariable("id") Long id) {
        MovimientoInventarioProductoTerminado x = ventaProductoTerminadoService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(x, MovimientoInventarioProductoTerminadoDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        ventaProductoTerminadoService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody MovimientoInventarioProductoTerminadoDTO dto) {
        ModelMapper m = new ModelMapper();
        MovimientoInventarioProductoTerminado x = m.map(dto, MovimientoInventarioProductoTerminado.class);
        ventaProductoTerminadoService.insert(x);
    }
}
