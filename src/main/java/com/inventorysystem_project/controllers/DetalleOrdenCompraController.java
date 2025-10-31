package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.DetalleOrdenCompraDTO;
import com.inventorysystem_project.entities.DetalleOrdenCompra;
import com.inventorysystem_project.serviceinterfaces.IDetalleOrdenCompraService;
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
@RequestMapping("/detalle-orden-compra")
public class DetalleOrdenCompraController {

    @Autowired
    private IDetalleOrdenCompraService detalleOrdenCompraService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody DetalleOrdenCompraDTO dto) {
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            DetalleOrdenCompra detalleOrdenCompra = m.map(dto, DetalleOrdenCompra.class);
            detalleOrdenCompraService.insert(detalleOrdenCompra);
            
            // Mapear la respuesta con el ID generado
            DetalleOrdenCompraDTO responseDTO = m.map(detalleOrdenCompra, DetalleOrdenCompraDTO.class);
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
    public List<DetalleOrdenCompraDTO> listar() {
        return detalleOrdenCompraService.list().stream().map(detalleOrdenCompra -> {
            ModelMapper m = new ModelMapper();
            return m.map(detalleOrdenCompra, DetalleOrdenCompraDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public DetalleOrdenCompraDTO listarPorId(@PathVariable("id") Long id) {
        DetalleOrdenCompra detalleOrdenCompra = detalleOrdenCompraService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(detalleOrdenCompra, DetalleOrdenCompraDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        detalleOrdenCompraService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody DetalleOrdenCompraDTO dto) {
        ModelMapper m = new ModelMapper();
        DetalleOrdenCompra detalleOrdenCompra = m.map(dto, DetalleOrdenCompra.class);
        detalleOrdenCompraService.insert(detalleOrdenCompra);
    }
}
