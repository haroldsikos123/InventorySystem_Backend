package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.repositories.EmpresaRepository;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaServiceImplement implements IEmpresaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public void insert(Empresa empresa) {
        empresaRepository.save(empresa);
    }

    @Override
    public List<Empresa> list() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa listId(Long id) {
        return empresaRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        empresaRepository.deleteById(id);
    }
}