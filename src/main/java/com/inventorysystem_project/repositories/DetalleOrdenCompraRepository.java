package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
