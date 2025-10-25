package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Usuario;

import java.util.List;

public interface IUsuarioService {
    void insert(Usuario user);
    List<Usuario> list();
    void delete(Long id);
    Usuario listId(Long id);

    // Puedes agregar métodos personalizados luego, como:
    // List<Users> findByEnabled(boolean status);
    // List<Users> findByRoleName(String role);
}