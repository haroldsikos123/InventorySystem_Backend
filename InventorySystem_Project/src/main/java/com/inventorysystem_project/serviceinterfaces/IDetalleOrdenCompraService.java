package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.DetalleOrdenCompra;
import java.util.List;

public interface IDetalleOrdenCompraService {

    void insert(DetalleOrdenCompra detalleOrdenCompra);

    List<DetalleOrdenCompra> list();

    void delete(Long id);

    DetalleOrdenCompra listId(Long id);
}
