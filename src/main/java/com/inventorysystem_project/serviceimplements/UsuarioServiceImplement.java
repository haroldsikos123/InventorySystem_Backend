package com.inventorysystem_project.serviceimplements; 
import com.inventorysystem_project.entities.Rol;
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.repositories.RolRepository;
import com.inventorysystem_project.repositories.UsuarioRepository;
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- AÑADIDO: acceso a roles para asignar GUEST por defecto ---
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void insert(Usuario usuario) {
        // Asignar rol GUEST por defecto si no se proporciona ninguno
        if (usuario.getRol() == null) {
            Rol rolGuest = rolRepository.findByRol("GUEST");
            if (rolGuest == null) {
                 // Si el rol GUEST no existe en la BD, se crea para asegurar que el registro funcione.
                 rolGuest = new Rol();
                 rolGuest.setRol("GUEST");
                 rolGuest = rolRepository.save(rolGuest);
            }
            usuario.setRol(rolGuest);
        }

        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> list() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario listId(Long id) {
        Usuario user = usuarioRepository.findById(id).orElse(null);
        if (user != null) {
            Hibernate.initialize(user.getRol()); // Esto sí es válido para inicializar lazy
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario findByUsername(String username) {
        Usuario user = usuarioRepository.findByUsername(username);
        if (user != null) {
            Hibernate.initialize(user.getRol());
        }
        return user;
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
