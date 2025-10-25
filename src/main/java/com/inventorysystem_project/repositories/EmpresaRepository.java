package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Puedes añadir métodos de consulta personalizados si los necesitas
    Empresa findByNombre(String nombre);
    Empresa findByRuc(Integer ruc);
}