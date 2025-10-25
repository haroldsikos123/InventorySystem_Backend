package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.DetalleOrdenCompraDTO;
import com.inventorysystem_project.entities.DetalleOrdenCompra;
import com.inventorysystem_project.serviceinterfaces.IDetalleOrdenCompraService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/detalle-orden-compra")
public class DetalleOrdenCompraController {

    @Autowired
    private IDetalleOrdenCompraService detalleOrdenCompraService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody DetalleOrdenCompraDTO dto) {
        ModelMapper m = new ModelMapper();
        DetalleOrdenCompra detalleOrdenCompra = m.map(dto, DetalleOrdenCompra.class);
        detalleOrdenCompraService.insert(detalleOrdenCompra);
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
