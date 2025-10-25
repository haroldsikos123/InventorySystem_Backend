package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Proveedor;
import com.inventorysystem_project.repositories.ProveedorRepository;
import com.inventorysystem_project.serviceinterfaces.IProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorServiceImplement implements IProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public void insert(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    @Override
    public List<Proveedor> list() {
        return proveedorRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public Proveedor listId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }
}
