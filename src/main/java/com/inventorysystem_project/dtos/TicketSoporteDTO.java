package com.inventorysystem_project.dtos;

// Importa los enums
import com.inventorysystem_project.entities.enums.EstadoTicket;
import com.inventorysystem_project.entities.enums.PrioridadTicket;
import com.inventorysystem_project.entities.enums.TipoTicket;
import java.time.LocalDateTime;

public class TicketSoporteDTO {
    private Long id;
    private String formattedId; // ID formateado como #INC-123
    private LocalDateTime fechaReporte;
    
    // Usamos IDs para la transferencia de datos
    private Long usuarioReportaId;
    private String usuarioReportaNombre; // Campo extra para mostrar en el front
    private String usuarioReportaUsername; // Campo para seguridad y validación
    
    private String descripcion;
    private PrioridadTicket prioridad;
    private EstadoTicket estado;
    
    // Usamos IDs para la transferencia de datos
    private Long responsableAsignadoId;
    private String responsableAsignadoNombre; // Campo extra para mostrar en el front
    
    private TipoTicket tipo;
    private String solucion;
    private LocalDateTime fechaCierre;

    // --- NUEVOS CAMPOS PARA CALIFICACIÓN Y SEGUIMIENTO DE TIEMPO ---
    private Integer calificacion;
    private LocalDateTime fechaInicioAtencion;
    private LocalDateTime fechaResolucion;
    private Long duracionAtencionMinutos;
    // --- FIN DE NUEVOS CAMPOS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(LocalDateTime fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public Long getUsuarioReportaId() {
        return usuarioReportaId;
    }

    public void setUsuarioReportaId(Long usuarioReportaId) {
        this.usuarioReportaId = usuarioReportaId;
    }

    public String getUsuarioReportaNombre() {
        return usuarioReportaNombre;
    }

    public void setUsuarioReportaNombre(String usuarioReportaNombre) {
        this.usuarioReportaNombre = usuarioReportaNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public PrioridadTicket getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadTicket prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }

    public Long getResponsableAsignadoId() {
        return responsableAsignadoId;
    }

    public void setResponsableAsignadoId(Long responsableAsignadoId) {
        this.responsableAsignadoId = responsableAsignadoId;
    }

    public String getResponsableAsignadoNombre() {
        return responsableAsignadoNombre;
    }

    public void setResponsableAsignadoNombre(String responsableAsignadoNombre) {
        this.responsableAsignadoNombre = responsableAsignadoNombre;
    }

    public TipoTicket getTipo() {
        return tipo;
    }

    public void setTipo(TipoTicket tipo) {
        this.tipo = tipo;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getUsuarioReportaUsername() {
        return usuarioReportaUsername;
    }

    public void setUsuarioReportaUsername(String usuarioReportaUsername) {
        this.usuarioReportaUsername = usuarioReportaUsername;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public LocalDateTime getFechaInicioAtencion() {
        return fechaInicioAtencion;
    }

    public void setFechaInicioAtencion(LocalDateTime fechaInicioAtencion) {
        this.fechaInicioAtencion = fechaInicioAtencion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Long getDuracionAtencionMinutos() {
        return duracionAtencionMinutos;
    }

    public void setDuracionAtencionMinutos(Long duracionAtencionMinutos) {
        this.duracionAtencionMinutos = duracionAtencionMinutos;
    }

    public String getFormattedId() {
        return formattedId;
    }

    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }

}