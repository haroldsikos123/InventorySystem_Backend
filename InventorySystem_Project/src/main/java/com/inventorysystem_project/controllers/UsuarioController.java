package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.UsuarioDTO;
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/registrar")
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);
        // Asumiendo que el DTO tiene el ID de la empresa y los roles como String
        usuarioService.insert(usuario);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<UsuarioDTO> listar() {
        return usuarioService.list().stream().map(usuario -> {
            ModelMapper m = new ModelMapper();
            return m.map(usuario, UsuarioDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public UsuarioDTO listarPorId(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(usuario, UsuarioDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        usuarioService.delete(id);
    }

    @PutMapping
    public void modificar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);
        usuarioService.insert(usuario);
    }
}
