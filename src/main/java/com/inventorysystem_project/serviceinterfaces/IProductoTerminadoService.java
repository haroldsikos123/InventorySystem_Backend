package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.ProductoTerminado;
import java.util.List;

public interface IProductoTerminadoService {

    void insert(ProductoTerminado productoTerminado);

    List<ProductoTerminado> list();

    void delete(Long id);

    ProductoTerminado listId(Long id);
}
