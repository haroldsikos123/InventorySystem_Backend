package com.inventorysystem_project.serviceimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventorysystem_project.entities.Almacen;
import com.inventorysystem_project.repositories.AlmacenRepository;
import com.inventorysystem_project.serviceinterfaces.IAlmacenService;

import java.util.List;

@Service
public class AlmacenServiceImplement implements IAlmacenService {
    @Autowired
    private AlmacenRepository almacenR;

    @Override
    public void insert(Almacen almacen) {
        almacenR.save(almacen);
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
}
