package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.MateriaPrima;
import java.util.List;

public interface IMateriaPrimaService {

    void insert(MateriaPrima materiaPrima);

    List<MateriaPrima> list();

    void delete(Long id);

    MateriaPrima listId(Long id);
}
