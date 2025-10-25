package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Rol;

import java.util.List;

public interface IRolService {
    public void insert(Rol rol);
    public List<Rol> list();
    public void delete(Long idRol);
    public Rol listId(Long idRol);
}
