package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.ErrorConocido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorConocidoRepository extends JpaRepository<ErrorConocido, Long> {
    // Puedes añadir búsquedas por estado si es necesario
    // List<ErrorConocido> findByEstado(EstadoProblema estado);
}