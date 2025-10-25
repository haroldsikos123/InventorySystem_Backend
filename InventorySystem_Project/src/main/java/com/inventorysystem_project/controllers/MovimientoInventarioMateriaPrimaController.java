package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.MovimientoInventarioMateriaPrimaDTO;
import com.inventorysystem_project.entities.MovimientoInventarioMateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IMovimientoInventarioMateriaPrimaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimientos-materia-prima")
public class MovimientoInventarioMateriaPrimaController {

    @Autowired
    private IMovimientoInventarioMateriaPrimaService movimientoService;

    // Registrar un nuevo movimiento
    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody MovimientoInventarioMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        MovimientoInventarioMateriaPrima movimiento = m.map(dto, MovimientoInventarioMateriaPrima.class);
        movimientoService.insert(movimiento);
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
