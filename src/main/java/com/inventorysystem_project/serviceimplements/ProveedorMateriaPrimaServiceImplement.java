package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.ProveedorMateriaPrima;
import com.inventorysystem_project.repositories.ProveedorMateriaPrimaRepository;
import com.inventorysystem_project.serviceinterfaces.IProveedorMateriaPrimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorMateriaPrimaServiceImplement implements IProveedorMateriaPrimaService {

    @Autowired
    private ProveedorMateriaPrimaRepository proveedorMateriaPrimaRepository;

    @Override
    public void insert(ProveedorMateriaPrima proveedorMateriaPrima) {
        proveedorMateriaPrimaRepository.save(proveedorMateriaPrima);
    }

    @Override
    public List<ProveedorMateriaPrima> list() {
        return proveedorMateriaPrimaRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        proveedorMateriaPrimaRepository.deleteById(id);
    }

    @Override
    public ProveedorMateriaPrima listId(Long id) {
        return proveedorMateriaPrimaRepository.findById(id).orElse(null);
    }
}
