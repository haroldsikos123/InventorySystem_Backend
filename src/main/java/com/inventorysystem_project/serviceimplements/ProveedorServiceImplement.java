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
        System.out.println("🔍 Intentando eliminar proveedor con ID: " + id);
        
        if (id == null) {
            throw new RuntimeException("El ID del proveedor no puede ser null");
        }
        
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
        
        System.out.println("✅ Proveedor encontrado: " + proveedor.getNombreEmpresaProveedor());
        
        // Verificar si existen órdenes de compra asociadas
        if (ordenCompraRepository.existsByProveedor(proveedor)) {
            long cantidadOrdenes = ordenCompraRepository.countByProveedor(proveedor);
            System.out.println("❌ Proveedor tiene " + cantidadOrdenes + " órdenes de compra asociadas");
            throw new DataIntegrityException(
                "No se puede eliminar el proveedor '" + proveedor.getNombreEmpresaProveedor() + 
                "' porque tiene " + cantidadOrdenes + 
                " orden(es) de compra asociada(s). Debe eliminar o reasignar las órdenes de compra primero."
            );
        }
        
        System.out.println("🗑️ Eliminando proveedor: " + proveedor.getNombreEmpresaProveedor());
        proveedorRepository.deleteById(id);
        System.out.println("✅ Proveedor eliminado exitosamente");
    }

    @Override
    public Proveedor listId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }
}
