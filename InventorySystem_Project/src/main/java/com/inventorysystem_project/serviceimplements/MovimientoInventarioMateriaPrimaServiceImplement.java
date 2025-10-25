package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.MovimientoInventarioMateriaPrima;
import com.inventorysystem_project.repositories.MovimientoInventarioMateriaPrimaRepository;
import com.inventorysystem_project.serviceinterfaces.IMovimientoInventarioMateriaPrimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MovimientoInventarioMateriaPrimaServiceImplement implements IMovimientoInventarioMateriaPrimaService {

    @Autowired
    private MovimientoInventarioMateriaPrimaRepository movimientoRepository;

    @Override
    public void insert(MovimientoInventarioMateriaPrima movimiento) {
        movimiento.setFechaMovimiento(new Date()); // <-- AÑADIR ESTA LÍNEA
        movimientoRepository.save(movimiento);
    }
    @Override
    public List<MovimientoInventarioMateriaPrima> list() {
        return movimientoRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        movimientoRepository.deleteById(id);
    }

    @Override
    public MovimientoInventarioMateriaPrima listId(Long id) {
        return movimientoRepository.findById(id).orElse(null);
    }
}
