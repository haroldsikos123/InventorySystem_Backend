package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Proveedor;
import java.util.List;

public interface IProveedorService {

    void insert(Proveedor proveedor);

    List<Proveedor> list();

    void delete(Long id);

    Proveedor listId(Long id);
}
