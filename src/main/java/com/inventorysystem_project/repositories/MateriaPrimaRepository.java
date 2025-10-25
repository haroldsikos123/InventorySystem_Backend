package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario
}
