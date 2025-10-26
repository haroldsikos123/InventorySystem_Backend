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
import java.util.List;
import java.util.stream.Collectors;

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
    public TicketSoporteDTO registrarTicket(TicketSoporteDTO ticketSoporteDTO) {
        TicketSoporte ticket = modelMapper.map(ticketSoporteDTO, TicketSoporte.class);

        Usuario usuarioReporta = usuarioRepo.findById(ticketSoporteDTO.getUsuarioReportaId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario (reporta) no encontrado con ID: " + ticketSoporteDTO.getUsuarioReportaId()));
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

        if (ticketSoporteDTO.getEstado() != null && ticketExistente.getEstado() != ticketSoporteDTO.getEstado()) {
            ticketExistente.setEstado(ticketSoporteDTO.getEstado());
            if (ticketSoporteDTO.getEstado() == EstadoTicket.CERRADO || ticketSoporteDTO.getEstado() == EstadoTicket.RESUELTO) {
                ticketExistente.setFechaCierre(LocalDateTime.now());
            } else {
                ticketExistente.setFechaCierre(null);
            }
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
    public ComentarioTicketDTO agregarComentario(Long ticketId, ComentarioTicketDTO comentarioDTO) {
        TicketSoporte ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + ticketId));
        
        Usuario usuario = usuarioRepo.findById(comentarioDTO.getUsuarioId())
                 .orElseThrow(() -> new EntityNotFoundException("Usuario (comenta) no encontrado con ID: " + comentarioDTO.getUsuarioId()));

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
}