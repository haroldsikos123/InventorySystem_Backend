package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.AlmacenDTO;
import com.inventorysystem_project.entities.Almacen;
import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.serviceinterfaces.IAlmacenService;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/almacenes")
public class AlmacenController {

    @Autowired
    private IAlmacenService almacenService;

    @Autowired
    private IEmpresaService empresaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody AlmacenDTO dto) {
        ModelMapper m = new ModelMapper();
        Almacen almacen = m.map(dto, Almacen.class);

        // Establecer la empresa manualmente ya que el DTO solo tiene el ID
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                almacen.setEmpresa(empresa);
            }
        }

        almacenService.insert(almacen);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<AlmacenDTO> listar() {
        return almacenService.list().stream().map(almacen -> {
            AlmacenDTO dto = new AlmacenDTO();
            dto.setId(almacen.getId());
            if (almacen.getEmpresa() != null) {
                dto.setEmpresaId(almacen.getEmpresa().getId());
            }
            dto.setNombre(almacen.getNombre());
            dto.setUbicacion(almacen.getUbicacion());
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public AlmacenDTO listarPorId(@PathVariable("id") Long id) {
        Almacen almacen = almacenService.listId(id);
        AlmacenDTO dto = new AlmacenDTO();
        dto.setId(almacen.getId());
        if (almacen.getEmpresa() != null) {
            dto.setEmpresaId(almacen.getEmpresa().getId());
        }
        dto.setNombre(almacen.getNombre());
        dto.setUbicacion(almacen.getUbicacion());
        return dto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        almacenService.delete(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody AlmacenDTO dto) {
        ModelMapper m = new ModelMapper();
        Almacen almacen = m.map(dto, Almacen.class);

        // Establecer la empresa manualmente ya que el DTO solo tiene el ID
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                almacen.setEmpresa(empresa);
            }
        }

        almacenService.insert(almacen);
    }
}
