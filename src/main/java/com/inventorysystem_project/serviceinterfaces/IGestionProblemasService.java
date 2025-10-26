package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.dtos.ErrorConocidoDTO;
import java.util.List;

public interface IGestionProblemasService {
    
    ErrorConocidoDTO registrarErrorConocido(ErrorConocidoDTO errorConocidoDTO);
    ErrorConocidoDTO actualizarErrorConocido(Long id, ErrorConocidoDTO errorConocidoDTO);
    ErrorConocidoDTO obtenerErrorPorId(Long id);
    List<ErrorConocidoDTO> listarErroresConocidos();
    
    // Opcional: Método para lógica de negocio más compleja
    // void analizarIncidentesParaProblemas(); 
}