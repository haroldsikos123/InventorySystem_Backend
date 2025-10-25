package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.StockAlmacenProductoTerminado;
import java.util.List;

public interface IStockAlmacenProductoTerminadoService {

    void insert(StockAlmacenProductoTerminado stockAlmacenProductoTerminado);

    List<StockAlmacenProductoTerminado> list();

    void delete(Long id);

    StockAlmacenProductoTerminado listId(Long id);
}
