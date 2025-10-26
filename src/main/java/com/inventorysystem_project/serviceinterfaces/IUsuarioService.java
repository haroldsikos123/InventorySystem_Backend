package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Usuario;
import java.util.List;

public interface IUsuarioService {
    void insert(Usuario user);
    List<Usuario> list();
    void delete(Long id);
    Usuario listId(Long id);
    Usuario findByUsername(String username);
    Usuario findByCorreo(String correo);
}