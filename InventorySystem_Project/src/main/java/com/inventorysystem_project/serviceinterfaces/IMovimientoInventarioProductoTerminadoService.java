package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.MovimientoInventarioProductoTerminado;
import java.util.List;

public interface IMovimientoInventarioProductoTerminadoService {

    void insert(MovimientoInventarioProductoTerminado movimientoInventarioProductoTerminado);

    List<MovimientoInventarioProductoTerminado> list();

    void delete(Long id);

    MovimientoInventarioProductoTerminado listId(Long id);
}
