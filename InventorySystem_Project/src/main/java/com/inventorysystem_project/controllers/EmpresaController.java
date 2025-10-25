package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.EmpresaDTO;
import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private IEmpresaService empresaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody EmpresaDTO dto) {
        ModelMapper m = new ModelMapper();
        Empresa empresa = m.map(dto, Empresa.class);
        empresaService.insert(empresa);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<EmpresaDTO> listar() {
        return empresaService.list().stream().map(empresa -> {
            ModelMapper m = new ModelMapper();
            return m.map(empresa, EmpresaDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public EmpresaDTO listarPorId(@PathVariable("id") Long id) {
        Empresa empresa = empresaService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(empresa, EmpresaDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        empresaService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody EmpresaDTO dto) {
        ModelMapper m = new ModelMapper();
        Empresa empresa = m.map(dto, Empresa.class);
        empresaService.insert(empresa);
    }
}