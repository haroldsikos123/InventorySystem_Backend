package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Almacen;
import java.util.List;

public interface IAlmacenService {
    public void insert(Almacen almacen);
    public List<Almacen> list();
    public void delete(Long id);
    public Almacen listId(Long id);
}
