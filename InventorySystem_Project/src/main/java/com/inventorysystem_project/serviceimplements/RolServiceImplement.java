package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import com.inventorysystem_project.entities.Rol;
import com.inventorysystem_project.repositories.RolRepository;
import com.inventorysystem_project.serviceinterfaces.IRolService;

import java.util.List;

@Service
public class RolServiceImplement implements IRolService {
    @Autowired
    private RolRepository rolR;

    @Override
    public void insert(Rol rol) {
        rolR.save(rol);
    }

    @Override
    public List<Rol> list() {
        return rolR.findAll();
    }

    @Override
    public void delete(Long idRol) { rolR.deleteById(idRol);}

    @Override
    public Rol listId(Long idRol) {
        return rolR.findById(idRol).orElse(null);
    }



}
