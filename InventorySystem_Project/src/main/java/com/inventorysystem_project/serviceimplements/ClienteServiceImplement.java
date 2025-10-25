package com.inventorysystem_project.serviceimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventorysystem_project.entities.Cliente;
import com.inventorysystem_project.repositories.ClienteRepository;
import com.inventorysystem_project.serviceinterfaces.IClienteService;

import java.util.List;

@Service
public class ClienteServiceImplement implements IClienteService {
    @Autowired
    private ClienteRepository clienteR;

    @Override
    public void insert(Cliente cliente) {
        clienteR.save(cliente);
    }

    @Override
    public List<Cliente> list() {
        return clienteR.findAll();
    }

    @Override
    public void delete(Long id) {
        clienteR.deleteById(id);
    }

    @Override
    public Cliente listId(Long id) {
        return clienteR.findById(id).orElse(null);
    }
}
