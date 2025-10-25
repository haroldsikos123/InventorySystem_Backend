package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.DetalleOrdenCompra;
import com.inventorysystem_project.repositories.DetalleOrdenCompraRepository;
import com.inventorysystem_project.serviceinterfaces.IDetalleOrdenCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleOrdenCompraServiceImplement implements IDetalleOrdenCompraService {

    @Autowired
    private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

    @Override
    public void insert(DetalleOrdenCompra detalleOrdenCompra) {
        detalleOrdenCompraRepository.save(detalleOrdenCompra);
    }

    @Override
    public List<DetalleOrdenCompra> list() {
        return detalleOrdenCompraRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        detalleOrdenCompraRepository.deleteById(id);
    }

    @Override
    public DetalleOrdenCompra listId(Long id) {
        return detalleOrdenCompraRepository.findById(id).orElse(null);
    }
}
