package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.ProveedorMateriaPrima;
import java.util.List;

public interface IProveedorMateriaPrimaService {

    void insert(ProveedorMateriaPrima proveedorMateriaPrima);

    List<ProveedorMateriaPrima> list();

    void delete(Long id);

    ProveedorMateriaPrima listId(Long id);
}
