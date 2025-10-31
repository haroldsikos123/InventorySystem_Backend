package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.StockAlmacenProductoTerminadoDTO;
import com.inventorysystem_project.entities.StockAlmacenProductoTerminado;
import com.inventorysystem_project.serviceinterfaces.IStockAlmacenProductoTerminadoService;
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
@RequestMapping("/stock-almacen-producto-terminado")
public class StockAlmacenProductoTerminadoController {

    @Autowired
    private IStockAlmacenProductoTerminadoService stockAlmacenProductoTerminadoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody StockAlmacenProductoTerminadoDTO dto) {
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            StockAlmacenProductoTerminado x = m.map(dto, StockAlmacenProductoTerminado.class);
            stockAlmacenProductoTerminadoService.insert(x);
            
            // Mapear la respuesta con el ID generado
            StockAlmacenProductoTerminadoDTO responseDTO = m.map(x, StockAlmacenProductoTerminadoDTO.class);
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
    public List<StockAlmacenProductoTerminadoDTO> listar() {
        return stockAlmacenProductoTerminadoService.list().stream().map(x -> {
            ModelMapper m = new ModelMapper();
            return m.map(x, StockAlmacenProductoTerminadoDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public StockAlmacenProductoTerminadoDTO listarPorId(@PathVariable("id") Long id) {
        StockAlmacenProductoTerminado x = stockAlmacenProductoTerminadoService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(x, StockAlmacenProductoTerminadoDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        stockAlmacenProductoTerminadoService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody StockAlmacenProductoTerminadoDTO dto) {
        ModelMapper m = new ModelMapper();
        StockAlmacenProductoTerminado x = m.map(dto, StockAlmacenProductoTerminado.class);
        stockAlmacenProductoTerminadoService.insert(x);
    }
}
