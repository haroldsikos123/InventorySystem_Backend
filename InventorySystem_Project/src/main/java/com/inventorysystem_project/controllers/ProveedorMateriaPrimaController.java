package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProveedorMateriaPrimaDTO;
import com.inventorysystem_project.entities.ProveedorMateriaPrima;
import com.inventorysystem_project.serviceinterfaces.IProveedorMateriaPrimaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveedor-materia-prima")
public class ProveedorMateriaPrimaController {

    @Autowired
    private IProveedorMateriaPrimaService proveedorMateriaPrimaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody ProveedorMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        ProveedorMateriaPrima x = m.map(dto, ProveedorMateriaPrima.class);
        proveedorMateriaPrimaService.insert(x);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ProveedorMateriaPrimaDTO> listar() {
        return proveedorMateriaPrimaService.list().stream().map(x -> {
            ModelMapper m = new ModelMapper();
            return m.map(x, ProveedorMateriaPrimaDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ProveedorMateriaPrimaDTO listarPorId(@PathVariable("id") Long id) {
        ProveedorMateriaPrima x = proveedorMateriaPrimaService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(x, ProveedorMateriaPrimaDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        proveedorMateriaPrimaService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ProveedorMateriaPrimaDTO dto) {
        ModelMapper m = new ModelMapper();
        ProveedorMateriaPrima x = m.map(dto, ProveedorMateriaPrima.class);
        proveedorMateriaPrimaService.insert(x);
    }
}
