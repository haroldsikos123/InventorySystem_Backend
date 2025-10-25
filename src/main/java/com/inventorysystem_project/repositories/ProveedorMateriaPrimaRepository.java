package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.ProveedorMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorMateriaPrimaRepository extends JpaRepository<ProveedorMateriaPrima, Long> {
    // Métodos personalizados si es necesario
}
