package com.inventorysystem_project.repositories;

import com.inventorysystem_project.entities.OrdenCompra;
import com.inventorysystem_project.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    
    // Método para verificar si existe alguna orden de compra para un proveedor específico
    boolean existsByProveedor(Proveedor proveedor);
    
    // Contar órdenes de compra por proveedor
    long countByProveedor(Proveedor proveedor);
}
