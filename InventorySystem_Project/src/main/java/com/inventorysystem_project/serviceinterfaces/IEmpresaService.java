package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Empresa;
import java.util.List;

public interface IEmpresaService {
    public void insert(Empresa empresa);
    public List<Empresa> list();
    public Empresa listId(Long id);
    public void delete(Long id);
}