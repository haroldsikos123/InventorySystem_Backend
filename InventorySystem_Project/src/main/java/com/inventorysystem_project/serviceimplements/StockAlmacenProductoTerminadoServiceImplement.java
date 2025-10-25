package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.StockAlmacenProductoTerminado;
import com.inventorysystem_project.repositories.StockAlmacenProductoTerminadoRepository;
import com.inventorysystem_project.serviceinterfaces.IStockAlmacenProductoTerminadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockAlmacenProductoTerminadoServiceImplement implements IStockAlmacenProductoTerminadoService {

    @Autowired
    private StockAlmacenProductoTerminadoRepository stockAlmacenProductoTerminadoRepository;

    @Override
    public void insert(StockAlmacenProductoTerminado stockAlmacenProductoTerminado) {
        stockAlmacenProductoTerminadoRepository.save(stockAlmacenProductoTerminado);
    }

    @Override
    public List<StockAlmacenProductoTerminado> list() {
        return stockAlmacenProductoTerminadoRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        stockAlmacenProductoTerminadoRepository.deleteById(id);
    }

    @Override
    public StockAlmacenProductoTerminado listId(Long id) {
        return stockAlmacenProductoTerminadoRepository.findById(id).orElse(null);
    }
}
