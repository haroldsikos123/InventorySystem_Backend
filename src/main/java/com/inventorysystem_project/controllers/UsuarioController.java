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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
    public ResponseEntity<?> registrar(@RequestBody UsuarioDTO dto) {
        try {
            dto.setId(null);
            
            if (usuarioService.findByUsername(dto.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El nombre de usuario '" + dto.getUsername() + "' ya existe"));
            }
            if (usuarioService.findByCorreo(dto.getCorreo()) != null) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                    .body(Map.of("error", "El correo '" + dto.getCorreo() + "' ya está registrado"));
            }
            
            ModelMapper m = new ModelMapper();
            Usuario usuario = m.map(dto, Usuario.class);

            try {
                Empresa empresaPorDefecto = empresaService.listId(1L); 
                usuario.setEmpresa(empresaPorDefecto); 
            } catch (Exception e) {
                usuario.setEmpresa(null);
            }

            usuario.setRol(null);
            usuarioService.insert(usuario);

            UsuarioDTO usuarioGuardadoDTO = m.map(usuario, UsuarioDTO.class);
            
            if (usuario.getRol() != null) {
                usuarioGuardadoDTO.setRolId(usuario.getRol().getId());
            }
            if (usuario.getEmpresa() != null) {
                usuarioGuardadoDTO.setEmpresaId(usuario.getEmpresa().getId());
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardadoDTO);
            
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Error de integridad de datos: " + ex.getMostSpecificCause().getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + ex.getMessage()));
        }
    }

    @GetMapping("/listar")
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

    @GetMapping("/asignables")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UsuarioDTO>> getUsuariosAsignables() {
        List<UsuarioDTO> listaUsuarios = usuarioService.getUsuariosAsignables();
        return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
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
            System.err.println("Error al eliminar usuario: " + e.getMessage());
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

        usuarioActual.setNombre(dto.getNombre());
        usuarioActual.setApellido(dto.getApellido());
        usuarioActual.setTelefono(dto.getTelefono());

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
    public ResponseEntity<?> modificar(@RequestBody UsuarioDTO dto, Authentication authentication) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("🔍 PUT /usuarios recibido");
        System.out.println("📝 Usuario a modificar ID: " + dto.getId());
        System.out.println("📝 Nombre: " + dto.getNombre());
        
        if (authentication != null) {
            System.out.println("✅ Authentication presente: " + authentication.getName());
            System.out.println("🔐 Authorities: " + authentication.getAuthorities());
            System.out.println("🎭 Authenticated: " + authentication.isAuthenticated());
        } else {
            System.out.println("❌ NO HAY AUTHENTICATION");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "No autenticado"));
        }
        System.out.println("═══════════════════════════════════════");
        
        try {
            if (dto.getId() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El ID del usuario es requerido"));
            }
            
            ModelMapper modelMapper = new ModelMapper();
            Usuario usuarioExistente = usuarioService.listId(dto.getId());
            
            if (usuarioExistente == null) {
                System.out.println("❌ Usuario con ID " + dto.getId() + " no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }

            System.out.println("✅ Usuario existente encontrado: " + usuarioExistente.getUsername());

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
                    System.err.println("Advertencia: Empresa con ID " + dto.getEmpresaId() + " no encontrada");
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
                    System.err.println("Advertencia: Rol con ID " + dto.getRolId() + " no encontrado");
                }
            } else {
                System.err.println("Advertencia: No se proporcionó rolId. Se mantendrá el rol actual.");
            }

            usuarioService.insert(usuarioExistente);
            Usuario usuarioGuardado = usuarioService.listId(dto.getId());
            UsuarioDTO usuarioActualizadoDTO = modelMapper.map(usuarioGuardado, UsuarioDTO.class);
            
            if (usuarioGuardado.getRol() != null) {
                usuarioActualizadoDTO.setRolId(usuarioGuardado.getRol().getId());
            }
            if (usuarioGuardado.getEmpresa() != null) {
                usuarioActualizadoDTO.setEmpresaId(usuarioGuardado.getEmpresa().getId());
            }
            
            System.out.println("✅ Usuario actualizado correctamente: " + usuarioGuardado.getUsername());
            return ResponseEntity.ok(usuarioActualizadoDTO);
            
        } catch(Exception e) {
            System.err.println("❌ Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar usuario: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> actualizarPassword(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        
        Usuario usuario = usuarioService.listId(id);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String newPassword = requestBody.get("password");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioActual = usuarioService.findByUsername(userDetails.getUsername());
        
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        boolean esPropioUsuario = usuarioActual != null && usuarioActual.getId().equals(id);
        
        if (!esAdmin && !esPropioUsuario) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            usuario.setPassword(newPassword);
            usuarioService.insert(usuario);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println("Error al actualizar contraseña del usuario ID " + id + ": " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}