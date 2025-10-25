package com.inventorysystem_project.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventorysystem_project.dtos.RolDTO;
import com.inventorysystem_project.entities.Rol;
import com.inventorysystem_project.serviceinterfaces.IRolService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RolController {
    @Autowired
    private IRolService rolR;

    @PostMapping("/registrar")
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody RolDTO dto){
        ModelMapper m=new ModelMapper();
        Rol d=m.map(dto, Rol.class);
        rolR.insert(d);
    }
    @GetMapping("/listar")
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<RolDTO> listar(){
        return rolR.list().stream().map(x->{
            ModelMapper m=new ModelMapper();
            return m.map(x,RolDTO.class);
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable("id")Long id){
        rolR.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody RolDTO dto){
        ModelMapper m=new ModelMapper();
        Rol d=m.map(dto, Rol.class);
        rolR.insert(d);
    }
}

