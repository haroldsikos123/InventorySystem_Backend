package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Reclamo;
import com.inventorysystem_project.repositories.ReclamoRepository;
import com.inventorysystem_project.serviceinterfaces.IReclamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamoServiceImplement implements IReclamoService {

    @Autowired
    private ReclamoRepository reclamoRepository;

    @Override
    public void insert(Reclamo reclamo) {
        reclamoRepository.save(reclamo);
    }

    @Override
    public List<Reclamo> list() {
        return reclamoRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        reclamoRepository.deleteById(id);
    }

    @Override
    public Reclamo listId(Long id) {
        return reclamoRepository.findById(id).orElse(null);
    }
}