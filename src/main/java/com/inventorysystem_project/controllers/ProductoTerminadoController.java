package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ProductoTerminadoDTO;
import com.inventorysystem_project.entities.ProductoTerminado;
import com.inventorysystem_project.serviceinterfaces.IProductoTerminadoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos-terminados")
public class ProductoTerminadoController {

    @Autowired
    private IProductoTerminadoService productoTerminadoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody ProductoTerminadoDTO dto) {
        ModelMapper m = new ModelMapper();
        ProductoTerminado productoTerminado = m.map(dto, ProductoTerminado.class);
        productoTerminadoService.insert(productoTerminado);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ProductoTerminadoDTO> listar() {
        return productoTerminadoService.list().stream().map(productoTerminado -> {
            ModelMapper m = new ModelMapper();
            return m.map(productoTerminado, ProductoTerminadoDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ProductoTerminadoDTO listarPorId(@PathVariable("id") Long id) {
        ProductoTerminado productoTerminado = productoTerminadoService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(productoTerminado, ProductoTerminadoDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        productoTerminadoService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ProductoTerminadoDTO dto) {
        ModelMapper m = new ModelMapper();
        ProductoTerminado productoTerminado = m.map(dto, ProductoTerminado.class);
        productoTerminadoService.insert(productoTerminado);
    }
}
