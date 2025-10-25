package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.OrdenCompra;
import com.inventorysystem_project.repositories.OrdenCompraRepository;
import com.inventorysystem_project.serviceinterfaces.IOrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenCompraServiceImplement implements IOrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Override
    public OrdenCompra insert(OrdenCompra ordenCompra) {
        return ordenCompraRepository.save(ordenCompra);
    }
    @Override
    public List<OrdenCompra> list() {
        return ordenCompraRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        ordenCompraRepository.deleteById(id);
    }

    @Override
    public OrdenCompra listId(Long id) {
        return ordenCompraRepository.findById(id).orElse(null);
    }
}
