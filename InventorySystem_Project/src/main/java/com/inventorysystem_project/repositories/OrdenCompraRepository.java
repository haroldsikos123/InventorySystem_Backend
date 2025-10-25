package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
