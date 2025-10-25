package com.inventorysystem_project.serviceimplements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.repositories.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTANTE: Añadir esta importación

//Clase 2
@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repo;

    @Override
    @Transactional(readOnly = true) // <-- IMPORTANTE: Añadir esta anotación
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repo.findByUsername(username);

        if(user == null) {
            // Se ha mejorado el mensaje de error para mayor claridad
            throw new UsernameNotFoundException(String.format("User %s not exists", username));
        }

        List<GrantedAuthority> roles = new ArrayList<>();

        // Esta línea ahora funcionará correctamente gracias a @Transactional
        roles.add(new SimpleGrantedAuthority(user.getRol().getRol()));


        UserDetails ud = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, roles);

        return ud;
    }
}
