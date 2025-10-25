package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.StockAlmacenMateriaPrima;
import com.inventorysystem_project.repositories.StockAlmacenMateriaPrimaRepository;
import com.inventorysystem_project.serviceinterfaces.IStockAlmacenMateriaPrimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockAlmacenMateriaPrimaServiceImplement implements IStockAlmacenMateriaPrimaService {

    @Autowired
    private StockAlmacenMateriaPrimaRepository stockAlmacenMateriaPrimaRepository;

    @Override
    public void insert(StockAlmacenMateriaPrima stockAlmacenMateriaPrima) {
        stockAlmacenMateriaPrimaRepository.save(stockAlmacenMateriaPrima);
    }

    @Override
    public List<StockAlmacenMateriaPrima> list() {
        return stockAlmacenMateriaPrimaRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        stockAlmacenMateriaPrimaRepository.deleteById(id);
    }

    @Override
    public StockAlmacenMateriaPrima listId(Long id) {
        return stockAlmacenMateriaPrimaRepository.findById(id).orElse(null);
    }
}
