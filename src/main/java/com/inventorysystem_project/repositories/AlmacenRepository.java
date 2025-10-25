package com.inventorysystem_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventorysystem_project.entities.Almacen;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    // Métodos personalizados si son necesarios
}
