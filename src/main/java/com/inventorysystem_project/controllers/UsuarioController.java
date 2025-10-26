package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.UsuarioDTO;
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private IEmpresaService empresaService; // <-- Inyecta IEmpresaService

    @PostMapping("/registrar")
    // @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')") // Descomenta si usas seguridad aquí
    // Cambiado para devolver ResponseEntity<UsuarioDTO>
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);

        // --- INICIO DE LA CORRECCIÓN ---
        // Asignar la Empresa manualmente
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                usuario.setEmpresa(empresa);
            } else {
                 // Opcional: Manejar el caso si la empresa no se encuentra
                 // Podrías lanzar una excepción o devolver un error
                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Ejemplo de respuesta de error
            }
        } else {
             // Opcional: Manejar el caso si no se envía empresaId
             // Podrías asignar una empresa por defecto o lanzar un error si es requerido
             // usuario.setEmpresa(null); // O manejar como error si es obligatorio
        }
         // --- FIN DE LA CORRECCIÓN ---


        usuarioService.insert(usuario); // Guardar el usuario

         // Convertir la entidad guardada de nuevo a DTO para la respuesta
        UsuarioDTO usuarioGuardadoDTO = m.map(usuario, UsuarioDTO.class);


         // Devolver el DTO del usuario creado con estado 201 Created
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

    @PutMapping
    public void modificar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);
        usuarioService.insert(usuario);
    }
}
