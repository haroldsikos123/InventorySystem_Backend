package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.ProductoTerminado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoTerminadoRepository extends JpaRepository<ProductoTerminado, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
