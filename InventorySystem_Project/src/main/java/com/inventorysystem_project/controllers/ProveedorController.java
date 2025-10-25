package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProveedorDTO;
import com.inventorysystem_project.entities.Proveedor;
import com.inventorysystem_project.serviceinterfaces.IProveedorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService proveedorService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody ProveedorDTO dto) {
        ModelMapper m = new ModelMapper();
        Proveedor proveedor = m.map(dto, Proveedor.class);
        proveedorService.insert(proveedor);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ProveedorDTO> listar() {
        return proveedorService.list().stream().map(proveedor -> {
            ModelMapper m = new ModelMapper();
            return m.map(proveedor, ProveedorDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ProveedorDTO listarPorId(@PathVariable("id") Long id) {
        Proveedor proveedor = proveedorService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(proveedor, ProveedorDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        proveedorService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ProveedorDTO dto) {
        ModelMapper m = new ModelMapper();
        Proveedor proveedor = m.map(dto, Proveedor.class);
        proveedorService.insert(proveedor);
    }
}
