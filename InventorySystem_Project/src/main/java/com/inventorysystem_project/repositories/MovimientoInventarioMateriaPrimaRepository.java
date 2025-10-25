package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.MovimientoInventarioMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoInventarioMateriaPrimaRepository extends JpaRepository<MovimientoInventarioMateriaPrima, Long> {

}
