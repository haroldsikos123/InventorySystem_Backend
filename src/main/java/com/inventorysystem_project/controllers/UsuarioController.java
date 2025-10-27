package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.UsuarioDTO;
import com.inventorysystem_project.entities.Empresa;
import com.inventorysystem_project.entities.Rol;
import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.serviceinterfaces.IEmpresaService;
import com.inventorysystem_project.serviceinterfaces.IRolService;
import com.inventorysystem_project.serviceinterfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- Importante
import org.springframework.http.ResponseEntity; // <-- Importante
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IRolService rolService;

 @PostMapping("/registrar")
    // @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        
        // 1. VALIDACIÓN DE DUPLICADOS
        if (usuarioService.findByUsername(dto.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (usuarioService.findByCorreo(dto.getCorreo()) != null) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED); 
        }
        ModelMapper m = new ModelMapper();
        Usuario usuario = m.map(dto, Usuario.class);

        // 2. LÓGICA DE EMPRESA POR DEFECTO (ID 1)
        try {
            Empresa empresaPorDefecto = empresaService.listId(1L); 
            usuario.setEmpresa(empresaPorDefecto); 
        } catch (Exception e) {
            usuario.setEmpresa(null);
        }

        // 3. LÓGICA DE ROL POR DEFECTO (ID 1)  <-- ¡AÑADIR ESTO!
        try {
            Rol rolPorDefecto = rolService.listId(1L); // Busca el Rol con ID 1
            usuario.setRol(rolPorDefecto); // Asigna el rol
        } catch (Exception e) {
            System.err.println("Error al asignar rol por defecto (ID 1): " + e.getMessage());
            usuario.setRol(null);
        }
        // FIN DE LA MODIFICACIÓN

        // 4. GUARDAR USUARIO
        usuarioService.insert(usuario); // El servicio encriptará la contraseña

        // 5. DEVOLVER RESPUESTA
        UsuarioDTO usuarioGuardadoDTO = m.map(usuario, UsuarioDTO.class);
        
        // Asignar IDs al DTO de respuesta
        if (usuario.getRol() != null) {
            usuarioGuardadoDTO.setRolId(usuario.getRol().getId());
        }
        if (usuario.getEmpresa() != null) {
            usuarioGuardadoDTO.setEmpresaId(usuario.getEmpresa().getId());
        }
        
        return new ResponseEntity<>(usuarioGuardadoDTO, HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public List<UsuarioDTO> listar() {
        return usuarioService.list().stream().map(usuario -> {
            ModelMapper m = new ModelMapper();
            UsuarioDTO dto = m.map(usuario, UsuarioDTO.class);
            if (usuario.getRol() != null) {
                dto.setRolId(usuario.getRol().getId());
            }
            if (usuario.getEmpresa() != null) {
                dto.setEmpresaId(usuario.getEmpresa().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    public ResponseEntity<UsuarioDTO> listarPorId(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.listId(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        ModelMapper m = new ModelMapper();
        UsuarioDTO dto = m.map(usuario, UsuarioDTO.class);
        if (usuario.getRol() != null) {
            dto.setRolId(usuario.getRol().getId());
        }
        if (usuario.getEmpresa() != null) {
            dto.setEmpresaId(usuario.getEmpresa().getId());
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        if (usuarioService.listId(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioActual = usuarioService.findByUsername(userDetails.getUsername());
        if (usuarioActual != null && usuarioActual.getId().equals(id)) {
            System.err.println("Advertencia: Intento de auto-eliminación del usuario ID " + id);
        }

        try {
            usuarioService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e) {
            System.err.println("Error al guardar usuario actualizado: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDTO> obtenerMiPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioActual = usuarioService.findByUsername(userDetails.getUsername());

        if (usuarioActual == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ModelMapper m = new ModelMapper();
        UsuarioDTO dto = m.map(usuarioActual, UsuarioDTO.class);
        if (usuarioActual.getRol() != null) {
            dto.setRolId(usuarioActual.getRol().getId());
        }
        if (usuarioActual.getEmpresa() != null) {
            dto.setEmpresaId(usuarioActual.getEmpresa().getId());
        }
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDTO> actualizarMiPerfil(@RequestBody UsuarioDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioActual = usuarioService.findByUsername(userDetails.getUsername());

        if (usuarioActual == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar solo campos permitidos
        usuarioActual.setNombre(dto.getNombre());
        usuarioActual.setApellido(dto.getApellido());
        usuarioActual.setTelefono(dto.getTelefono());
        // NO permitir cambiar rol, enabled, username, etc.

        try {
            usuarioService.insert(usuarioActual);
            Usuario usuarioGuardado = usuarioService.listId(usuarioActual.getId());
            ModelMapper m = new ModelMapper();
            UsuarioDTO updatedDto = m.map(usuarioGuardado, UsuarioDTO.class);
            if (usuarioGuardado.getRol() != null) {
                updatedDto.setRolId(usuarioGuardado.getRol().getId());
            }
            if (usuarioGuardado.getEmpresa() != null) {
                updatedDto.setEmpresaId(usuarioGuardado.getEmpresa().getId());
            }
            return ResponseEntity.ok(updatedDto);
        } catch (Exception e) {
            System.err.println("Error al actualizar perfil de usuario: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UsuarioDTO> modificar(@RequestBody UsuarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();

        Usuario usuarioExistente = usuarioService.listId(dto.getId());
        if (usuarioExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Mapear campos actualizables
        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setApellido(dto.getApellido());
        usuarioExistente.setCorreo(dto.getCorreo());
        usuarioExistente.setDni(dto.getDni());
        usuarioExistente.setEnabled(dto.getEnabled());
        usuarioExistente.setFechaNacimiento(dto.getFechaNacimiento());
        usuarioExistente.setGenero(dto.getGenero());
        usuarioExistente.setTelefono(dto.getTelefono());

        // Lógica de Empresa
        if (dto.getEmpresaId() != null) {
            Empresa empresa = empresaService.listId(dto.getEmpresaId());
            if (empresa != null) {
                usuarioExistente.setEmpresa(empresa);
            } else {
                System.err.println("Advertencia: Empresa con ID " + dto.getEmpresaId() + " no encontrada al modificar usuario " + dto.getId());
                usuarioExistente.setEmpresa(null);
            }
        } else {
            usuarioExistente.setEmpresa(null);
        }

        // Lógica de Rol
        if (dto.getRolId() != null) {
            Rol nuevoRol = rolService.listId(dto.getRolId());
            if (nuevoRol != null) {
                usuarioExistente.setRol(nuevoRol);
            } else {
                System.err.println("Advertencia: Rol con ID " + dto.getRolId() + " no encontrado al modificar usuario " + dto.getId());
            }
        } else {
            System.err.println("Advertencia: No se proporcionó rolId al modificar usuario " + dto.getId() + ". Se mantendrá el rol actual.");
        }

        try {
            usuarioService.insert(usuarioExistente);
            Usuario usuarioGuardado = usuarioService.listId(dto.getId());
            UsuarioDTO usuarioActualizadoDTO = modelMapper.map(usuarioGuardado, UsuarioDTO.class);
            if (usuarioGuardado.getRol() != null) {
                usuarioActualizadoDTO.setRolId(usuarioGuardado.getRol().getId());
            }
            return new ResponseEntity<>(usuarioActualizadoDTO, HttpStatus.OK);
        } catch(Exception e) {
            System.err.println("Error al guardar usuario actualizado: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}