package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.Usuario;
import com.inventorysystem_project.entities.SolicitudCambio;
import com.inventorysystem_project.entities.enums.EstadoCambio;
import com.inventorysystem_project.entities.enums.TipoCambio;
import com.inventorysystem_project.repositories.UsuarioRepository;
import com.inventorysystem_project.repositories.SolicitudCambioRepository;
import com.inventorysystem_project.dtos.SolicitudCambioDTO;
import com.inventorysystem_project.serviceinterfaces.IGestionCambiosService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestionCambiosServiceImplement implements IGestionCambiosService {

    @Autowired
    private SolicitudCambioRepository rfcRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ModelMapper modelMapper;
    
    private SolicitudCambioDTO convertToDto(SolicitudCambio rfc) {
        SolicitudCambioDTO dto = modelMapper.map(rfc, SolicitudCambioDTO.class);
        if (rfc.getSolicitante() != null) {
            dto.setSolicitanteId(rfc.getSolicitante().getId());
            dto.setSolicitanteNombre(rfc.getSolicitante().getNombre() + " " + rfc.getSolicitante().getApellido());
        }
        return dto;
    }

    @Override
    public SolicitudCambioDTO registrarRFC(SolicitudCambioDTO solicitudCambioDTO, String usernameSolicitante) {
        SolicitudCambio rfc = modelMapper.map(solicitudCambioDTO, SolicitudCambio.class);

        Usuario solicitante = usuarioRepo.findByUsername(usernameSolicitante);
        if (solicitante == null) {
            throw new EntityNotFoundException("Usuario (solicitante) no encontrado con username: " + usernameSolicitante);
        }
        rfc.setSolicitante(solicitante);

        rfc.setEstado(EstadoCambio.REGISTRADO);

        SolicitudCambio rfcGuardada = rfcRepo.save(rfc);
        return convertToDto(rfcGuardada);
    }

    @Override
    public SolicitudCambioDTO actualizarRFC(Long id, SolicitudCambioDTO solicitudCambioDTO) {
         SolicitudCambio rfcExistente = rfcRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de Cambio (RFC) no encontrada con ID: " + id));

        rfcExistente.setTitulo(solicitudCambioDTO.getTitulo());
        rfcExistente.setDescripcion(solicitudCambioDTO.getDescripcion());
        rfcExistente.setJustificacion(solicitudCambioDTO.getJustificacion());
        rfcExistente.setImpacto(solicitudCambioDTO.getImpacto());
        rfcExistente.setRiesgos(solicitudCambioDTO.getRiesgos());
        rfcExistente.setPlanImplementacion(solicitudCambioDTO.getPlanImplementacion());
        rfcExistente.setPlanRetirada(solicitudCambioDTO.getPlanRetirada());
        rfcExistente.setTipoCambio(solicitudCambioDTO.getTipoCambio());

        SolicitudCambio rfcActualizada = rfcRepo.save(rfcExistente);
        return convertToDto(rfcActualizada);
    }

    @Override
    public SolicitudCambioDTO aprobarCambio(Long rfcId, Long aprobadorId, String tipoAprobacion) {
        SolicitudCambio rfc = rfcRepo.findById(rfcId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de Cambio (RFC) no encontrada con ID: " + rfcId));
        
        if (!usuarioRepo.existsById(aprobadorId)) {
             throw new EntityNotFoundException("Usuario (aprobador) no encontrado con ID: " + aprobadorId);
        }

        EstadoCambio nuevoEstado;
        if ("CAB".equalsIgnoreCase(tipoAprobacion)) {
            if(rfc.getTipoCambio() != TipoCambio.NORMAL) {
                 throw new IllegalArgumentException("El CAB solo puede aprobar cambios de tipo 'NORMAL'.");
            }
            nuevoEstado = EstadoCambio.APROBADO_CAB;
            
        } else if ("PM".equalsIgnoreCase(tipoAprobacion)) {
             if(rfc.getTipoCambio() != TipoCambio.EMERGENCIA) {
                 throw new IllegalArgumentException("El PM solo puede aprobar cambios de tipo 'EMERGENCIA'.");
            }
            nuevoEstado = EstadoCambio.APROBADO_PM_EMERGENCIA;
        } else {
            throw new IllegalArgumentException("Tipo de aprobación inválido: " + tipoAprobacion + ". Debe ser 'CAB' o 'PM'.");
        }

        rfc.setEstado(nuevoEstado);
        SolicitudCambio rfcGuardada = rfcRepo.save(rfc);
        return convertToDto(rfcGuardada);
    }

    @Override
    public SolicitudCambioDTO cambiarEstadoRFC(Long rfcId, String estadoStr) {
        SolicitudCambio rfc = rfcRepo.findById(rfcId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de Cambio (RFC) no encontrada con ID: " + rfcId));

        EstadoCambio nuevoEstado;
        try {
            nuevoEstado = EstadoCambio.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado de cambio inválido: " + estadoStr);
        }
        
        if (nuevoEstado == EstadoCambio.APROBADO_CAB || nuevoEstado == EstadoCambio.APROBADO_PM_EMERGENCIA) {
            throw new IllegalArgumentException("Use el método 'aprobarCambio' para aprobaciones.");
        }

        rfc.setEstado(nuevoEstado);
        SolicitudCambio rfcGuardada = rfcRepo.save(rfc);
        return convertToDto(rfcGuardada);
    }

    @Override
    public SolicitudCambioDTO obtenerRFCPorId(Long id) {
        SolicitudCambio rfc = rfcRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de Cambio (RFC) no encontrada con ID: " + id));
        return convertToDto(rfc);
    }

    @Override
    public List<SolicitudCambioDTO> listarRFCs() {
         return rfcRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudCambioDTO> listarRFCsPorTipo(String tipoStr) {
        TipoCambio tipo;
        try {
            tipo = TipoCambio.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de cambio inválido: " + tipoStr);
        }
        
        return rfcRepo.findByTipoCambio(tipo).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}