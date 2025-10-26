package com.inventorysystem_project.serviceinterfaces;

import com.inventorysystem_project.dtos.SolicitudCambioDTO;
import java.util.List;

public interface IGestionCambiosService {
    
    SolicitudCambioDTO registrarRFC(SolicitudCambioDTO solicitudCambioDTO);
    SolicitudCambioDTO actualizarRFC(Long id, SolicitudCambioDTO solicitudCambioDTO);
    
    // Método específico para el flujo de aprobación
    SolicitudCambioDTO aprobarCambio(Long rfcId, Long aprobadorId, String tipoAprobacion); // tipoAprobacion: "CAB" o "PM"
    
    // Método genérico para cambiar estado (ej. Implementado, Cerrado, Rechazado)
    SolicitudCambioDTO cambiarEstadoRFC(Long rfcId, String estado);
    
    SolicitudCambioDTO obtenerRFCPorId(Long id);
    List<SolicitudCambioDTO> listarRFCs();
    List<SolicitudCambioDTO> listarRFCsPorTipo(String tipo);
}