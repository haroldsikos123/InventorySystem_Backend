package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Cliente;
import java.util.List;

public interface IClienteService {
    public void insert(Cliente cliente);
    public List<Cliente> list();
    public void delete(Long id);
    public Cliente listId(Long id);
}
