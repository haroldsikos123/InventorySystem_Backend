package com.inventorysystem_project.controllers;

// --- IMPORTACIONES QUE FALTAN ---
import com.inventorysystem_project.dtos.UsuarioDTO;
import com.inventorysystem_project.entities.Empresa; // <-- Te falta este
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService; // <-- Te falta este
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- Te falta este
import org.springframework.http.ResponseEntity; // <-- Te falta este
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
// --- FIN DE IMPORTACIONES ---

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    private IEmpresaService empresaService; // <-- Inyecta IEmpresaService

@   PostMapping("/registrar")
    // @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);

        // --- INICIO DE CORRECCIÓN (Lógica de Empresa por Defecto) ---
        // Requerimiento: Asignar Empresa con ID 1 por defecto al crear.
        // Si la empresa con ID 1 no existe, se asignará null.
        try {
            // Buscamos la empresa con ID 1 (1L es para forzar que sea tipo Long)
            Empresa empresaPorDefecto = empresaService.listId(1L); 
            
            // listId(1L) devolverá la empresa si la encuentra,
            // o devolverá null si no la encuentra.
            // En ambos casos, es el comportamiento que deseas.
            usuario.setEmpresa(empresaPorDefecto); 
            
        } catch (Exception e) {
            // Si ocurre un error inesperado en el servicio (ej. la BD está caída),
            // también asignamos null para asegurar que el usuario se cree.
            usuario.setEmpresa(null);
        }
        // --- FIN DE CORRECCIÓN ---

        usuarioService.insert(usuario); // Guardar el usuario nuevo

        // Devolver el usuario creado
        UsuarioDTO usuarioGuardadoDTO = m.map(usuario, UsuarioDTO.class);
        return new ResponseEntity<>(usuarioGuardadoDTO, HttpStatus.CREATED);
    }
    
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<UsuarioDTO> listar() {
        return usuarioService.list().stream().map(usuario -> {
            ModelMapper m = new ModelMapper();
            return m.map(usuario, UsuarioDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public UsuarioDTO listarPorId(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(usuario, UsuarioDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        usuarioService.delete(id);
    }

    // --- MÉTODO MODIFICAR CORREGIDO ---
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<UsuarioDTO> modificar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        
        Usuario usuarioExistente = usuarioService.listId(dto.getId());

        if (usuarioExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }

        // Mapear campos actualizables
        // (Sería mejor mapear campo por campo para evitar problemas con la contraseña)
        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setApellido(dto.getApellido());
        usuarioExistente.setCorreo(dto.getCorreo());
        usuarioExistente.setDni(dto.getDni());
        usuarioExistente.setEnabled(dto.getEnabled());
        usuarioExistente.setFechaNacimiento(dto.getFechaNacimiento());
        usuarioExistente.setGenero(dto.getGenero());
        usuarioExistente.setTelefono(dto.getTelefono());
        // No mapeamos la contraseña aquí a menos que tengas lógica para re-encriptarla
        
        // Lógica de Empresa
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                usuarioExistente.setEmpresa(empresa);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
        } else {
            usuarioExistente.setEmpresa(null); 
        }

        usuarioService.insert(usuarioExistente); 

        UsuarioDTO usuarioActualizadoDTO = m.map(usuarioExistente, UsuarioDTO.class);
        return new ResponseEntity<>(usuarioActualizadoDTO, HttpStatus.OK);
    }
}