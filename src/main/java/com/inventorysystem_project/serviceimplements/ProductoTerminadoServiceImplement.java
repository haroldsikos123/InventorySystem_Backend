package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.ProductoTerminado;
import com.inventorysystem_project.repositories.ProductoTerminadoRepository;
import com.inventorysystem_project.serviceinterfaces.IProductoTerminadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoTerminadoServiceImplement implements IProductoTerminadoService {

    @Autowired
    private ProductoTerminadoRepository productoTerminadoRepository;

    @Override
    public void insert(ProductoTerminado productoTerminado) {
        productoTerminadoRepository.save(productoTerminado);
    }

    @Override
    public List<ProductoTerminado> list() {
        return productoTerminadoRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        productoTerminadoRepository.deleteById(id);
    }

    @Override
    public ProductoTerminado listId(Long id) {
        return productoTerminadoRepository.findById(id).orElse(null);
    }
}
