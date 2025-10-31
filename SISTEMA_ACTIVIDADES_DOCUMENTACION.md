# 📋 Sistema de Actividades para Tickets - DOCUMENTACIÓN COMPLETA

## **🎯 Resumen del Sistema**

El sistema de actividades proporciona un **seguimiento completo y auditoria** de todas las acciones realizadas sobre los tickets de soporte, integrando tanto actividades automáticas del sistema como comentarios manuales de los usuarios.

---

## **🏗️ Arquitectura del Sistema**

### **1. Entity: TicketActividad.java**
```java
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
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_actividad", nullable = false)
    private LocalDateTime fechaActividad;
}
```

### **2. Repository: TicketActividadRepository.java**
```java
@Repository
public interface TicketActividadRepository extends JpaRepository<TicketActividad, Long> {
    List<TicketActividad> findByTicketSoporteIdOrderByFechaActividadDesc(Long ticketId);
}
```

### **3. DTO: ActividadCombinadaDTO.java**
```java
public class ActividadCombinadaDTO {
    private String tipo;           // "ACTIVIDAD" o "COMENTARIO"
    private String descripcion;
    private String usuarioNombre;
    private LocalDateTime fecha;
}
```

---

## **📊 Base de Datos**

### **Tabla: ticket_actividad**
```sql
CREATE TABLE IF NOT EXISTS ticket_actividad (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    usuario_id BIGINT,
    descripcion TEXT NOT NULL,
    fecha_actividad TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    
    -- Claves foráneas
    CONSTRAINT fk_ticket_actividad_ticket 
        FOREIGN KEY (ticket_id) 
        REFERENCES ticket_soporte(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_ticket_actividad_usuario 
        FOREIGN KEY (usuario_id) 
        REFERENCES usuario(id) 
        ON DELETE SET NULL
);

-- Índices para rendimiento
CREATE INDEX IF NOT EXISTS idx_ticket_actividad_ticket_id ON ticket_actividad(ticket_id);
CREATE INDEX IF NOT EXISTS idx_ticket_actividad_fecha ON ticket_actividad(fecha_actividad DESC);
```

---

## **🔄 Funcionalidades Automáticas**

### **Actividades Registradas Automáticamente:**

1. **Creación de Ticket**
   - `"Ticket creado por [Nombre Usuario]"`

2. **Asignación de Responsable**
   - `"Responsable asignado: [Nombre]"` (creación inicial)
   - `"Responsable asignado cambiado de '[Anterior]' a '[Nuevo]'"`

3. **Cambios de Estado**
   - `"Estado cambiado de [EstadoAnterior] a [EstadoNuevo]"`
   - `"Inicio de atención del ticket"` (ABIERTO → EN_PROGRESO)
   - `"Ticket resuelto - Duración: X minutos"` (→ RESUELTO)
   - `"Ticket cerrado"` (→ CERRADO)
   - `"Ticket reabierto"` (→ ABIERTO)

---

## **🛠️ Implementación en el Servicio**

### **Métodos Helper**
```java
// Registrar actividad con usuario específico
private void registrarActividad(TicketSoporte ticket, Long usuarioId, String descripcion) {
    // Implementación con usuario específico
}

// Registrar actividad con usuario del contexto de seguridad
private void registrarActividad(TicketSoporte ticket, String descripcion) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Usuario usuarioActual = usuarioRepo.findByUsername(username);
    Long usuarioId = (usuarioActual != null) ? usuarioActual.getId() : null;
    registrarActividad(ticket, usuarioId, descripcion);
}
```

### **Integración en Operaciones**

**Creación de Ticket:**
```java
TicketSoporte ticketGuardado = ticketRepo.save(ticket);
registrarActividad(ticketGuardado, "Ticket creado por " + usuarioReporta.getNombre() + " " + usuarioReporta.getApellido());
```

**Actualización de Estado:**
```java
if (estadoAnterior != estadoNuevo) {
    String descripcion = String.format("Estado cambiado de %s a %s", estadoAnterior, estadoNuevo);
    registrarActividad(ticketExistente, descripcion);
}
```

---

## **📡 Endpoint de API**

### **GET /api/tickets/{id}/actividades**

**Descripción:** Obtiene la línea de tiempo combinada de actividades y comentarios para un ticket específico.

**Respuesta:**
```json
[
  {
    "tipo": "ACTIVIDAD",
    "descripcion": "Ticket creado por Juan Pérez",
    "usuarioNombre": "Juan Pérez",
    "fecha": "2024-12-19T10:30:00"
  },
  {
    "tipo": "COMENTARIO", 
    "descripcion": "Revisando el problema reportado",
    "usuarioNombre": "María García",
    "fecha": "2024-12-19T11:15:00"
  },
  {
    "tipo": "ACTIVIDAD",
    "descripcion": "Estado cambiado de ABIERTO a EN_PROGRESO",
    "usuarioNombre": "María García", 
    "fecha": "2024-12-19T11:20:00"
  }
]
```

**Seguridad:** Requiere autenticación. Solo usuarios con acceso al ticket pueden ver las actividades.

---

## **⚡ Beneficios del Sistema**

### **1. Auditoría Completa**
- Registro automático de todas las acciones críticas
- Trazabilidad completa del ciclo de vida del ticket
- Identificación del usuario responsable de cada cambio

### **2. Transparencia**
- Visibilidad total para todos los stakeholders
- Historial cronológico de eventos
- Facilita la identificación de cuellos de botella

### **3. Cumplimiento ITIL**
- Registra métricas de tiempo de atención
- Documenta cambios de responsabilidad
- Facilita análisis de performance y SLA

### **4. Experiencia de Usuario**
- Timeline unificado de actividades y comentarios
- Información contextual sobre cambios
- Mejor comprensión del progreso del ticket

---

## **🚀 Próximos Pasos**

### **Implementación Inmediata:**
1. **Ejecutar el script SQL** `crear_tabla_ticket_actividad.sql` en PostgreSQL
2. **Reiniciar la aplicación** para cargar las nuevas entidades
3. **Probar el endpoint** `/api/tickets/{id}/actividades`

### **Mejoras Futuras:**
1. **Notificaciones Push** cuando se registran nuevas actividades
2. **Filtros por Tipo** de actividad (solo sistema, solo comentarios)
3. **Export/Import** del historial de actividades
4. **Métricas Avanzadas** basadas en el historial de actividades

---

## **🧪 Testing**

### **Casos de Prueba Recomendados:**

1. **Crear un ticket nuevo**
   - ✅ Verificar actividad "Ticket creado"
   - ✅ Si hay responsable asignado, verificar "Responsable asignado"

2. **Cambiar estado del ticket**
   - ✅ Verificar actividad de cambio de estado
   - ✅ Para EN_PROGRESO: verificar "Inicio de atención"
   - ✅ Para RESUELTO: verificar duración calculada

3. **Reasignar responsable**
   - ✅ Verificar actividad con nombres anterior y nuevo

4. **Timeline combinado**
   - ✅ Agregar comentarios y actividades
   - ✅ Verificar orden cronológico correcto
   - ✅ Verificar tipos diferenciados

---

## **📝 Notas Técnicas**

- **Performance:** Índices creados en `ticket_id` y `fecha_actividad`
- **Seguridad:** Usuario puede ser NULL para actividades del sistema
- **Escalabilidad:** Paginación recomendada para tickets con mucha actividad
- **Integridad:** ON DELETE CASCADE elimina actividades si se elimina el ticket

---

*Sistema implementado siguiendo mejores prácticas de ITIL v4 y Spring Boot 3.4.4*