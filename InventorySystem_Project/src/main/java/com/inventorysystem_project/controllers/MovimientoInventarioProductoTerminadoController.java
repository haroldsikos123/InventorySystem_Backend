package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.MovimientoInventarioProductoTerminadoDTO;
import com.inventorysystem_project.entities.MovimientoInventarioProductoTerminado;
import com.inventorysystem_project.serviceinterfaces.IMovimientoInventarioProductoTerminadoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimientos-producto-terminado")
public class MovimientoInventarioProductoTerminadoController {

    @Autowired
    private IMovimientoInventarioProductoTerminadoService ventaProductoTerminadoService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody MovimientoInventarioProductoTerminadoDTO dto) {
        ModelMapper m = new ModelMapper();
        MovimientoInventarioProductoTerminado x = m.map(dto, MovimientoInventarioProductoTerminado.class);
        ventaProductoTerminadoService.insert(x);
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
