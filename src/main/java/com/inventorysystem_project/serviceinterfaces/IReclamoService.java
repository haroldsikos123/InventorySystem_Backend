package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Reclamo;
import java.util.List;

public interface IReclamoService {

    void insert(Reclamo reclamo);

    List<Reclamo> list();

    void delete(Long id);

    Reclamo listId(Long id);
}