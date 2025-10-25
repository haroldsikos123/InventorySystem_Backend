package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.MovimientoInventarioMateriaPrima;

import java.util.List;

public interface IMovimientoInventarioMateriaPrimaService {

    void insert(MovimientoInventarioMateriaPrima movimiento);

    List<MovimientoInventarioMateriaPrima> list();

    void delete(Long id);

    MovimientoInventarioMateriaPrima listId(Long id);



}
