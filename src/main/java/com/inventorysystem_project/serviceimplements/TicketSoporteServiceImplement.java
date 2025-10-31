package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.entities.TicketSoporte;
import com.inventorysystem_project.entities.ComentarioTicket;
import com.inventorysystem_project.entities.enums.EstadoTicket;
import com.inventorysystem_project.repositories.UsuarioRepository;
import com.inventorysystem_project.repositories.TicketSoporteRepository;
import com.inventorysystem_project.repositories.ComentarioTicketRepository;
import com.inventorysystem_project.dtos.TicketSoporteDTO;
import com.inventorysystem_project.dtos.ComentarioTicketDTO;
import com.inventorysystem_project.serviceinterfaces.ITicketSoporteService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketSoporteServiceImplement implements ITicketSoporteService {

    @Autowired
    private TicketSoporteRepository ticketRepo;
    
    @Autowired
    private ComentarioTicketRepository comentarioRepo;
    
    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Autowired
    private ModelMapper modelMapper;

    private TicketSoporteDTO convertToDto(TicketSoporte ticket) {
        TicketSoporteDTO dto = modelMapper.map(ticket, TicketSoporteDTO.class);
        
        if (ticket.getUsuarioReporta() != null) {
            dto.setUsuarioReportaId(ticket.getUsuarioReporta().getId());
            dto.setUsuarioReportaNombre(ticket.getUsuarioReporta().getNombre() + " " + ticket.getUsuarioReporta().getApellido());
            dto.setUsuarioReportaUsername(ticket.getUsuarioReporta().getUsername());
        }
        
        if (ticket.getResponsableAsignado() != null) {
            dto.setResponsableAsignadoId(ticket.getResponsableAsignado().getId());
            dto.setResponsableAsignadoNombre(ticket.getResponsableAsignado().getNombre() + " " + ticket.getResponsableAsignado().getApellido());
        }
        return dto;
    }

    private ComentarioTicketDTO convertToDto(ComentarioTicket comentario) {
        ComentarioTicketDTO dto = modelMapper.map(comentario, ComentarioTicketDTO.class);
        
        if (comentario.getTicket() != null) {
            dto.setTicketId(comentario.getTicket().getId());
        }
         
        if (comentario.getUsuario() != null) {
            dto.setUsuarioId(comentario.getUsuario().getId());
            dto.setUsuarioNombre(comentario.getUsuario().getNombre() + " " + comentario.getUsuario().getApellido());
        }
        return dto;
    }

    @Override
    public TicketSoporteDTO registrarTicket(TicketSoporteDTO ticketSoporteDTO, String usernameReporta) {
        TicketSoporte ticket = modelMapper.map(ticketSoporteDTO, TicketSoporte.class);

        Usuario usuarioReporta = usuarioRepo.findByUsername(usernameReporta);
        if (usuarioReporta == null) {
            throw new EntityNotFoundException("Usuario (reporta) no encontrado con username: " + usernameReporta);
        }
        ticket.setUsuarioReporta(usuarioReporta);

        if (ticketSoporteDTO.getResponsableAsignadoId() != null) {
             Usuario responsable = usuarioRepo.findById(ticketSoporteDTO.getResponsableAsignadoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario (responsable) no encontrado con ID: " + ticketSoporteDTO.getResponsableAsignadoId()));
             ticket.setResponsableAsignado(responsable);
        }

        ticket.setFechaReporte(LocalDateTime.now());
        ticket.setEstado(EstadoTicket.ABIERTO);

        TicketSoporte ticketGuardado = ticketRepo.save(ticket);
        return convertToDto(ticketGuardado);
    }

    @Override
    public TicketSoporteDTO actualizarTicket(Long id, TicketSoporteDTO ticketSoporteDTO) {
        TicketSoporte ticketExistente = ticketRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + id));

        // Guardamos el estado anterior para comparar
        EstadoTicket estadoAnterior = ticketExistente.getEstado();

        ticketExistente.setDescripcion(ticketSoporteDTO.getDescripcion());
        ticketExistente.setPrioridad(ticketSoporteDTO.getPrioridad());
        ticketExistente.setTipo(ticketSoporteDTO.getTipo());
        ticketExistente.setSolucion(ticketSoporteDTO.getSolucion());

        if (ticketSoporteDTO.getResponsableAsignadoId() != null &&
            (ticketExistente.getResponsableAsignado() == null || !ticketExistente.getResponsableAsignado().getId().equals(ticketSoporteDTO.getResponsableAsignadoId()))) {
             
             Usuario nuevoResponsable = usuarioRepo.findById(ticketSoporteDTO.getResponsableAsignadoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario (responsable) no encontrado con ID: " + ticketSoporteDTO.getResponsableAsignadoId()));
             ticketExistente.setResponsableAsignado(nuevoResponsable);
        }

        // Actualizar estado si cambió
        if (ticketSoporteDTO.getEstado() != null && ticketExistente.getEstado() != ticketSoporteDTO.getEstado()) {
            EstadoTicket estadoNuevo = ticketSoporteDTO.getEstado();
            ticketExistente.setEstado(estadoNuevo);

            // 1. LÓGICA DE INICIO DE ATENCIÓN
            // Si el ticket estaba Abierto y pasa a "EN_PROGRESO"
            // Y si aún no se ha registrado un inicio de atención
            if (estadoAnterior == EstadoTicket.ABIERTO &&
                estadoNuevo == EstadoTicket.EN_PROGRESO &&
                ticketExistente.getFechaInicioAtencion() == null) {
                
                ticketExistente.setFechaInicioAtencion(LocalDateTime.now());
            }

            // 2. LÓGICA DE RESOLUCIÓN Y DURACIÓN
            // Si el ticket NO estaba Resuelto y ahora SÍ lo está
            if (estadoAnterior != EstadoTicket.RESUELTO && estadoNuevo == EstadoTicket.RESUELTO) {
                
                LocalDateTime fechaResolucion = LocalDateTime.now();
                ticketExistente.setFechaResolucion(fechaResolucion);
                ticketExistente.setFechaCierre(fechaResolucion);

                // Verificamos si se había iniciado la atención formalmente
                LocalDateTime fechaInicio = ticketExistente.getFechaInicioAtencion();
                
                if (fechaInicio == null) {
                    // Si se resolvió directamente (sin pasar por "EN_PROGRESO"),
                    // usamos la fecha de creación como inicio de la atención
                    fechaInicio = ticketExistente.getFechaReporte();
                    ticketExistente.setFechaInicioAtencion(fechaInicio);
                }

                // Calculamos la duración en minutos
                long duracionEnMinutos = Duration.between(fechaInicio, fechaResolucion).toMinutes();
                ticketExistente.setDuracionAtencionMinutos(duracionEnMinutos);
            }

            // Lógica original para otros estados
            if (estadoNuevo == EstadoTicket.CERRADO && estadoAnterior != EstadoTicket.CERRADO) {
                ticketExistente.setFechaCierre(LocalDateTime.now());
            } else if (estadoNuevo != EstadoTicket.CERRADO && estadoNuevo != EstadoTicket.RESUELTO) {
                // Si se reabre, limpiar fecha de cierre
                ticketExistente.setFechaCierre(null);
            }
        }

        // Actualizar calificación si se proporciona
        if (ticketSoporteDTO.getCalificacion() != null) {
            ticketExistente.setCalificacion(ticketSoporteDTO.getCalificacion());
        }

        TicketSoporte ticketActualizado = ticketRepo.save(ticketExistente);
        return convertToDto(ticketActualizado);
    }

    @Override
    public TicketSoporteDTO asignarTicket(Long ticketId, Long responsableId) {
        TicketSoporte ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + ticketId));
        
        Usuario responsable = usuarioRepo.findById(responsableId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario (responsable) no encontrado con ID: " + responsableId));

        ticket.setResponsableAsignado(responsable);
        
        if (ticket.getEstado() == EstadoTicket.ABIERTO) {
            ticket.setEstado(EstadoTicket.EN_PROGRESO);
        }
        
        TicketSoporte ticketGuardado = ticketRepo.save(ticket);
        return convertToDto(ticketGuardado);
    }

    @Override
    public TicketSoporteDTO cambiarEstado(Long ticketId, String estadoStr) {
        TicketSoporte ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + ticketId));

        EstadoTicket nuevoEstado;
        try {
            nuevoEstado = EstadoTicket.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de ticket inválido: " + estadoStr);
        }

        ticket.setEstado(nuevoEstado);
        
        if (nuevoEstado == EstadoTicket.CERRADO || nuevoEstado == EstadoTicket.RESUELTO) {
            if (ticket.getFechaCierre() == null) {
                 ticket.setFechaCierre(LocalDateTime.now());
            }
        } else {
            ticket.setFechaCierre(null);
        }

        TicketSoporte ticketGuardado = ticketRepo.save(ticket);
        return convertToDto(ticketGuardado);
    }

    @Override
    public TicketSoporteDTO obtenerTicketPorId(Long id) {
        TicketSoporte ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + id));
        return convertToDto(ticket);
    }

    @Override
    public List<TicketSoporteDTO> listarTodos() {
        return ticketRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketSoporteDTO> listarPorEstado(String estadoStr) {
         EstadoTicket estado;
        try {
            estado = EstadoTicket.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de ticket inválido: " + estadoStr);
        }
        
        return ticketRepo.findByEstado(estado).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarTicket(Long id) {
        TicketSoporte ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + id));
        ticketRepo.delete(ticket);
    }

    @Override
    public ComentarioTicketDTO agregarComentario(Long ticketId, ComentarioTicketDTO comentarioDTO, String usernameComenta) {
        TicketSoporte ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + ticketId));
        
        Usuario usuario = usuarioRepo.findByUsername(usernameComenta);
        if (usuario == null) {
            throw new EntityNotFoundException("Usuario (comenta) no encontrado con username: " + usernameComenta);
        }

        ComentarioTicket comentario = modelMapper.map(comentarioDTO, ComentarioTicket.class);
        comentario.setTicket(ticket);
        comentario.setUsuario(usuario);
        comentario.setFechaCreacion(LocalDateTime.now());

        ComentarioTicket comentarioGuardado = comentarioRepo.save(comentario);
        return convertToDto(comentarioGuardado);
    }

    @Override
    public List<ComentarioTicketDTO> listarComentariosPorTicket(Long ticketId) {
        if (!ticketRepo.existsById(ticketId)) {
             throw new EntityNotFoundException("Ticket no encontrado con ID: " + ticketId);
        }
        
        return comentarioRepo.findByTicketId(ticketId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TicketSoporteDTO> listarTicketsPorUsuario(Long usuarioId, String estadoStr) {
        List<TicketSoporte> tickets;

        if (estadoStr != null && !estadoStr.trim().isEmpty()) {
            EstadoTicket estado;
            try {
                estado = EstadoTicket.valueOf(estadoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de ticket inválido para filtrar: '" + estadoStr + "'");
            }
            tickets = ticketRepo.findByUsuarioReportaIdAndEstado(usuarioId, estado);
        } else {
            tickets = ticketRepo.findByUsuarioReportaId(usuarioId);
        }

        return tickets.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}