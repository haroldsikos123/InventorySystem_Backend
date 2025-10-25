package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.StockAlmacenMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAlmacenMateriaPrimaRepository extends JpaRepository<StockAlmacenMateriaPrima, Long> {
    // Métodos personalizados si es necesario
}
