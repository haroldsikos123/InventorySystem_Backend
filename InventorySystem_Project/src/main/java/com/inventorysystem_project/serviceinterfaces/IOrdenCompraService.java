package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.OrdenCompra;
import java.util.List;

public interface IOrdenCompraService {

    public OrdenCompra insert(OrdenCompra ordenCompra);

    List<OrdenCompra> list();

    void delete(Long id);

    OrdenCompra listId(Long id);
}
