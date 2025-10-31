package com.inventorysystem_project.serviceimplements;

import com.inventorysystem_project.entities.ErrorConocido;
import com.inventorysystem_project.repositories.ErrorConocidoRepository;
import com.inventorysystem_project.dtos.ErrorConocidoDTO;
import com.inventorysystem_project.serviceinterfaces.IGestionProblemasService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestionProblemasServiceImplement implements IGestionProblemasService {

    @Autowired
    private ErrorConocidoRepository errorRepo;

    @Autowired
    private ModelMapper modelMapper;

    private ErrorConocidoDTO convertToDto(ErrorConocido error) {
        ErrorConocidoDTO dto = modelMapper.map(error, ErrorConocidoDTO.class);
        
        // --- LÓGICA AÑADIDA PARA ID FORMATEADO ---
        if (error.getId() != null) {
            dto.setFormattedId("#PRB-" + error.getId());
        }
        // --- FIN LÓGICA AÑADIDA ---
        
        return dto;
    }

    @Override
    public ErrorConocidoDTO registrarErrorConocido(ErrorConocidoDTO errorConocidoDTO) {
        ErrorConocido error = modelMapper.map(errorConocidoDTO, ErrorConocido.class);
        ErrorConocido errorGuardado = errorRepo.save(error);
        return convertToDto(errorGuardado);
    }

    @Override
    public ErrorConocidoDTO actualizarErrorConocido(Long id, ErrorConocidoDTO errorConocidoDTO) {
        ErrorConocido errorExistente = errorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error Conocido no encontrado con ID: " + id));

        errorExistente.setDescripcionError(errorConocidoDTO.getDescripcionError());
        errorExistente.setSintomas(errorConocidoDTO.getSintomas());
        errorExistente.setCausaRaiz(errorConocidoDTO.getCausaRaiz());
        errorExistente.setSolucionTemporal(errorConocidoDTO.getSolucionTemporal());
        errorExistente.setSolucionDefinitivaPropuesta(errorConocidoDTO.getSolucionDefinitivaPropuesta());
        errorExistente.setEstado(errorConocidoDTO.getEstado());

        ErrorConocido errorActualizado = errorRepo.save(errorExistente);
        return convertToDto(errorActualizado);
    }

    @Override
    public ErrorConocidoDTO obtenerErrorPorId(Long id) {
        ErrorConocido error = errorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error Conocido no encontrado con ID: " + id));
        return convertToDto(error);
    }

    @Override
    public List<ErrorConocidoDTO> listarErroresConocidos() {
        return errorRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}