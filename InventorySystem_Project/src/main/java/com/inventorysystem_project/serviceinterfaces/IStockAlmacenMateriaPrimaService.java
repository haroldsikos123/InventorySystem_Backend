package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.StockAlmacenMateriaPrima;
import java.util.List;

public interface IStockAlmacenMateriaPrimaService {

    void insert(StockAlmacenMateriaPrima stockAlmacenMateriaPrima);

    List<StockAlmacenMateriaPrima> list();

    void delete(Long id);

    StockAlmacenMateriaPrima listId(Long id);
}
