package com.inventorysystem_project.serviceimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inventorysystem_project.entities.Almacen;
import com.inventorysystem_project.repositories.AlmacenRepository;
import com.inventorysystem_project.repositories.MovimientoInventarioMateriaPrimaRepository;
import com.inventorysystem_project.repositories.MovimientoInventarioProductoTerminadoRepository;
import com.inventorysystem_project.serviceinterfaces.IAlmacenService;

import java.util.List;

@Service
public class AlmacenServiceImplement implements IAlmacenService {
    @Autowired
    private AlmacenRepository almacenR;

    @Autowired
    private MovimientoInventarioMateriaPrimaRepository movimientoMPRepository;

    @Autowired
    private MovimientoInventarioProductoTerminadoRepository movimientoPTRepository;

    @Override
    @Transactional
    public void insert(Almacen almacen) {
        if (almacen.getId() != null && almacen.getId() > 0) {
            // Es una actualización - usar merge para reflejar cambios
            almacenR.save(almacen);
        } else {
            // Es un insert nuevo
            almacenR.save(almacen);
        }
    }

    @Override
    public List<Almacen> list() {
        return almacenR.findAll();
    }

    @Override
    public void delete(Long id) {
        almacenR.deleteById(id);
    }

    @Override
    public Almacen listId(Long id) {
        return almacenR.findById(id).orElse(null);
    }

    @Override
    public boolean tieneMovimientosRegistrados(Long almacenId) {
        long movimientosMP = movimientoMPRepository.countByAlmacenId(almacenId);
        long movimientosPT = movimientoPTRepository.countByAlmacenId(almacenId);
        return (movimientosMP + movimientosPT) > 0;
    }

    @Override
    public long contarMovimientos(Long almacenId) {
        long movimientosMP = movimientoMPRepository.countByAlmacenId(almacenId);
        long movimientosPT = movimientoPTRepository.countByAlmacenId(almacenId);
        return movimientosMP + movimientosPT;
    }
}
