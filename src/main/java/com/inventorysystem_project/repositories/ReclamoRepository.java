package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}