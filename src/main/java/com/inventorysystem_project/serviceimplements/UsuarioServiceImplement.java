package com.inventorysystem_project.services;

import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.repositories.UsuarioRepository;
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void insert(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> list() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario listId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}
