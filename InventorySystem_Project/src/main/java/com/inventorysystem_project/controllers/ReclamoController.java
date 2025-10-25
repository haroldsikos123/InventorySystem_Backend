package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ReclamoDTO;
import com.inventorysystem_project.entities.Reclamo;
import com.inventorysystem_project.serviceinterfaces.IReclamoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reclamo")
public class ReclamoController {

    @Autowired
    private IReclamoService reclamoService;

    // Registrar un nuevo reclamo
    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody ReclamoDTO dto) {
        ModelMapper m = new ModelMapper();
        Reclamo reclamo = m.map(dto, Reclamo.class);
        reclamoService.insert(reclamo);
    }

    // Listar todos los reclamos
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ReclamoDTO> listar() {
        return reclamoService.list().stream().map(reclamo -> {
            ModelMapper m = new ModelMapper();
            return m.map(reclamo, ReclamoDTO.class);
        }).collect(Collectors.toList());
    }

    // Obtener un reclamo por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ReclamoDTO listarPorId(@PathVariable("id") Long id) {
        Reclamo reclamo = reclamoService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(reclamo, ReclamoDTO.class);
    }

    // Eliminar un reclamo
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        reclamoService.delete(id);
    }

    // Modificar un reclamo
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ReclamoDTO dto) {
        ModelMapper m = new ModelMapper();
        Reclamo reclamo = m.map(dto, Reclamo.class);
        reclamoService.insert(reclamo);
    }
}