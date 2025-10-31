package com.inventorysystem_project.entities;

// Usamos jakarta.persistence como en tu entidad ComentarioTicket
import jakarta.persistence.*; 
import java.time.LocalDateTime; // Usamos LocalDateTime

@Entity
@Table(name = "ticket_actividad")
public class TicketActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketSoporte ticketSoporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id") // Usuario que realizó la acción
    private Usuario usuario;

    @Column(name = "descripcion", nullable = false)
    private String descripcion; 

    @Column(name = "fecha_actividad", nullable = false)
    private LocalDateTime fechaActividad;

    // Constructores
    public TicketActividad() {}

    public TicketActividad(TicketSoporte ticketSoporte, Usuario usuario, String descripcion, LocalDateTime fechaActividad) {
        this.ticketSoporte = ticketSoporte;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fechaActividad = fechaActividad;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketSoporte getTicketSoporte() {
        return ticketSoporte;
    }

    public void setTicketSoporte(TicketSoporte ticketSoporte) {
        this.ticketSoporte = ticketSoporte;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(LocalDateTime fechaActividad) {
        this.fechaActividad = fechaActividad;
    }
}