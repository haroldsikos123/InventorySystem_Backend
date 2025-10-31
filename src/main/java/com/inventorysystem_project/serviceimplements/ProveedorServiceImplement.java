package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Proveedor;
import com.inventorysystem_project.exceptions.DataIntegrityException;
import com.inventorysystem_project.repositories.OrdenCompraRepository;
import com.inventorysystem_project.repositories.ProveedorRepository;
import com.inventorysystem_project.serviceinterfaces.IProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorServiceImplement implements IProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Override
    public void insert(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    @Override
    public List<Proveedor> list() {
        return proveedorRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
        
        // Verificar si existen órdenes de compra asociadas
        if (ordenCompraRepository.existsByProveedor(proveedor)) {
            long cantidadOrdenes = ordenCompraRepository.countByProveedor(proveedor);
            throw new DataIntegrityException(
                "No se puede eliminar el proveedor '" + proveedor.getNombreEmpresaProveedor() + 
                "' porque tiene " + cantidadOrdenes + 
                " orden(es) de compra asociada(s). Debe eliminar o reasignar las órdenes de compra primero."
            );
        }
        
        proveedorRepository.deleteById(id);
    }

    @Override
    public Proveedor listId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }
}
