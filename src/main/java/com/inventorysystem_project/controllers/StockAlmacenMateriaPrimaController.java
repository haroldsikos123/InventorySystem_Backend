package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.StockAlmacenMateriaPrimaDTO;
import com.inventorysystem_project.entities.StockAlmacenMateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IStockAlmacenMateriaPrimaService;
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
@RequestMapping("/stock-almacen-materia-prima")
public class StockAlmacenMateriaPrimaController {

    @Autowired
    private IStockAlmacenMateriaPrimaService stockAlmacenMateriaPrimaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<?> registrar(@RequestBody StockAlmacenMateriaPrimaDTO dto) {
        try {
            // PROTECCIÓN CONTRA DUPLICACIÓN DE ID
            dto.setId(null);
            
            ModelMapper m = new ModelMapper();
            StockAlmacenMateriaPrima x = m.map(dto, StockAlmacenMateriaPrima.class);
            stockAlmacenMateriaPrimaService.insert(x);
            
            // Mapear la respuesta con el ID generado
            StockAlmacenMateriaPrimaDTO responseDTO = m.map(x, StockAlmacenMateriaPrimaDTO.class);
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
    public List<StockAlmacenMateriaPrimaDTO> listar() {
        return stockAlmacenMateriaPrimaService.list().stream().map(x -> {
            ModelMapper m = new ModelMapper();
            return m.map(x, StockAlmacenMateriaPrimaDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public StockAlmacenMateriaPrimaDTO listarPorId(@PathVariable("id") Long id) {
        StockAlmacenMateriaPrima x = stockAlmacenMateriaPrimaService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(x, StockAlmacenMateriaPrimaDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        stockAlmacenMateriaPrimaService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody StockAlmacenMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        StockAlmacenMateriaPrima x = m.map(dto, StockAlmacenMateriaPrima.class);
        stockAlmacenMateriaPrimaService.insert(x);
    }
}
