package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.StockAlmacenMateriaPrimaDTO;
import com.inventorysystem_project.entities.StockAlmacenMateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IStockAlmacenMateriaPrimaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock-almacen-materia-prima")
public class StockAlmacenMateriaPrimaController {

    @Autowired
    private IStockAlmacenMateriaPrimaService stockAlmacenMateriaPrimaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody StockAlmacenMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        StockAlmacenMateriaPrima x = m.map(dto, StockAlmacenMateriaPrima.class);
        stockAlmacenMateriaPrimaService.insert(x);
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
