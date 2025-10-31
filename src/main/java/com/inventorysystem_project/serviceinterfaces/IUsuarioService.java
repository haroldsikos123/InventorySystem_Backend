package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.dtos.UsuarioDTO;
import java.util.List;

public interface IUsuarioService {
    void insert(Usuario user);
    List<Usuario> list();
    void delete(Long id);
    Usuario listId(Long id);
    Usuario findByUsername(String username);
    Usuario findByCorreo(String correo);
    
    /**
     * Devuelve solo los usuarios que pueden ser asignados a tickets
     * (todos excepto GUEST y USER).
     */
    List<UsuarioDTO> getUsuariosAsignables();
}