package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.ClienteDTO;
import com.inventorysystem_project.entities.Cliente;
import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.serviceinterfaces.IClienteService;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IEmpresaService empresaService;

    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void registrar(@RequestBody ClienteDTO dto) {
        ModelMapper m = new ModelMapper();
        Cliente cliente = m.map(dto, Cliente.class);

        // Establecer la empresa manualmente ya que el DTO solo tiene el ID
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                cliente.setEmpresa(empresa);
            }
        }

        clienteService.insert(cliente);
    }

    @GetMapping("/listar")  // Ruta para listar todos los clientes
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<ClienteDTO> listar() {
        return clienteService.list().stream().map(cliente -> {
            ModelMapper m = new ModelMapper();
            return m.map(cliente, ClienteDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")  // Ruta para obtener un cliente por ID
    public ClienteDTO listarPorId(@PathVariable("id") Long id) {
        Cliente cliente = clienteService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(cliente, ClienteDTO.class);
    }

    @DeleteMapping("/{id}")  // Ruta para eliminar un cliente por ID
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        clienteService.delete(id);
    }

    @PutMapping  // Ruta para modificar un cliente
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody ClienteDTO dto) {
        ModelMapper m = new ModelMapper();
        Cliente cliente = m.map(dto, Cliente.class);

        // Establecer la empresa manualmente ya que el DTO solo tiene el ID
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                cliente.setEmpresa(empresa);
            }
        }

        clienteService.insert(cliente);
    }
}
