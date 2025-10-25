package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Se pueden agregar consultas personalizadas aquí si es necesario
}
