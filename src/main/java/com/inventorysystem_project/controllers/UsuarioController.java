package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.UsuarioDTO;
import com.inventorysystem_project.entities.Empresa; // <-- Importante
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService; // <-- Importante
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- Importante
import org.springframework.http.ResponseEntity; // <-- Importante
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
    private IEmpresaService empresaService; // Inyección del servicio de Empresa

    @PostMapping("/registrar")
    // @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        
        // 1. VALIDACIÓN DE DUPLICADOS
        if (usuarioService.findByUsername(dto.getUsername()) != null) {
            // Devuelve un error 409 Conflict si el usuario ya existe
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (usuarioService.findByCorreo(dto.getCorreo()) != null) {
            // Usamos un código distinto para que el frontend lo identifique
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED); 
        }
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);

        // 2. LÓGICA DE EMPRESA POR DEFECTO (ID 1)
        try {
            // Busca la empresa con ID 1
            Empresa empresaPorDefecto = empresaService.listId(1L); 
            usuario.setEmpresa(empresaPorDefecto); // Será null si no la encuentra
        } catch (Exception e) {
            // Si hay un error (ej. servicio no disponible), asigna null
            usuario.setEmpresa(null);
        }

        // 3. GUARDAR USUARIO
        usuarioService.insert(usuario); // El servicio se encarga de encriptar la contraseña

        // 4. DEVOLVER RESPUESTA
        UsuarioDTO usuarioGuardadoDTO = m.map(usuario, UsuarioDTO.class);
        return new ResponseEntity<>(usuarioGuardadoDTO, HttpStatus.CREATED); // Devuelve 201 Created
    }

    @GetMapping("/listar")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('GUEST')")
    public List<UsuarioDTO> listar() {
        System.out.println("Listar usuarios llamado!");
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

    // MÉTODO MODIFICAR (PUT) CORREGIDO
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<UsuarioDTO> modificar(@RequestBody UsuarioDTO dto) {
        ModelMapper m = new ModelMapper();
        
        // Buscar el usuario existente
        Usuario usuarioExistente = usuarioService.listId(dto.getId());
        if (usuarioExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // No encontrado
        }

        // Mapear campos actualizables (Evita pisar la contraseña si no se envía)
        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setApellido(dto.getApellido());
        usuarioExistente.setCorreo(dto.getCorreo());
        usuarioExistente.setDni(dto.getDni());
        usuarioExistente.setEnabled(dto.getEnabled());
        usuarioExistente.setFechaNacimiento(dto.getFechaNacimiento());
        usuarioExistente.setGenero(dto.getGenero());
        usuarioExistente.setTelefono(dto.getTelefono());

        // Lógica de Empresa (igual que en registrar, pero usando el ID del DTO)
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                usuarioExistente.setEmpresa(empresa);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Empresa ID inválido
            }
        } else {
            usuarioExistente.setEmpresa(null); // Permite quitar la empresa
        }

        // Guardar la entidad actualizada
        usuarioService.insert(usuarioExistente); 

        UsuarioDTO usuarioActualizadoDTO = m.map(usuarioExistente, UsuarioDTO.class);
        return new ResponseEntity<>(usuarioActualizadoDTO, HttpStatus.OK); // Devuelve 200 OK
    }
}