package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.MovimientoInventarioProductoTerminado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoInventarioProductoTerminadoRepository extends JpaRepository<MovimientoInventarioProductoTerminado, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
