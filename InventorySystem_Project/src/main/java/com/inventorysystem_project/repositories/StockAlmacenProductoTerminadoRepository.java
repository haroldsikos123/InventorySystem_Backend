package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.StockAlmacenProductoTerminado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAlmacenProductoTerminadoRepository extends JpaRepository<StockAlmacenProductoTerminado, Long> {
    // Métodos personalizados si es necesario
}
