package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.MateriaPrima;
import com.inventorysystem_project.repositories.MateriaPrimaRepository;
import com.inventorysystem_project.serviceinterfaces.IMateriaPrimaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaPrimaServiceImplement implements IMateriaPrimaService {

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Override
    public void insert(MateriaPrima materiaPrima) {
        materiaPrimaRepository.save(materiaPrima);
    }

    @Override
    public List<MateriaPrima> list() {
        return materiaPrimaRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        materiaPrimaRepository.deleteById(id);
    }

    @Override
    public MateriaPrima listId(Long id) {
        return materiaPrimaRepository.findById(id).orElse(null);
    }



}
