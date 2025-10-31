# ✅ ENDPOINTS DE TICKETS - AUTORIZACIÓN GRANULAR

## **🎯 Problema Resuelto**

**Error anterior**: `PUT http://localhost:8080/api/soporte/tickets/54 401 (Unauthorized)`

**Causa**: Restricciones de roles no apropiadas - USER y GUEST tenían permisos excesivos.

## **🔧 Soluciones Implementadas**

### **1. Autorización Granular por Tipo de Usuario**

#### **👥 Usuarios con Permisos Completos:**
```java
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SOPORTE_N1') or hasAuthority('SOPORTE_N2') or hasAuthority('GESTOR_CAMBIOS') or hasAuthority('CAB_MEMBER') or hasAuthority('PROJECT_MANAGER')")
```

#### **🚫 Usuarios con Permisos Limitados:**
- **USER**: Solo puede actualizar descripción/solución de sus propios tickets
- **GUEST**: Solo puede actualizar descripción/solución de sus propios tickets

### **2. Endpoints con Autorización Granular**

#### **🔧 Para Roles de Soporte (ADMIN, SOPORTE_N1, SOPORTE_N2, etc.):**
- ✅ `PUT /api/soporte/tickets/{id}` - **Actualizar ticket completo**
- ✅ `PUT /api/soporte/ticketsoporte/{id}` - **Actualizar ticket completo (compatibilidad)**
- ✅ `PUT /api/soporte/tickets/{ticketId}/estado` - **Cambiar estados**
- ✅ `PUT /api/soporte/tickets/{ticketId}/asignar/{responsableId}` - **Asignar responsables**

#### **👤 Para Usuarios Limitados (USER, GUEST):**
- ✅ `PATCH /api/soporte/tickets/{id}/descripcion` - **Solo descripción/solución de sus propios tickets**
- ❌ `PUT /api/soporte/tickets/{id}` - **Prohibido (401 Unauthorized)**
- ❌ `PUT /api/soporte/tickets/{ticketId}/estado` - **Prohibido (401 Unauthorized)**

#### **📋 Para Todos los Usuarios Autenticados:**
- ✅ `GET /api/soporte/tickets` - **Listar tickets**
- ✅ `POST /api/soporte/tickets` - **Crear tickets**
- ✅ `GET /api/soporte/tickets/{id}/actividades` - **Ver actividades**
- ✅ `GET /api/soporte/ticketsoporte/{id}/actividades` - **Ver actividades (compatibilidad)**

## **🎮 Cambios de Estado Ahora Permitidos**

### **Transiciones Libres:**
- ✅ `ABIERTO` → `EN_PROGRESO`
- ✅ `EN_PROGRESO` → `RESUELTO` 
- ✅ `RESUELTO` → `EN_PROGRESO` ← **AHORA FUNCIONA**
- ✅ `RESUELTO` → `ABIERTO` ← **AHORA FUNCIONA**
- ✅ `CERRADO` → `ABIERTO` ← **AHORA FUNCIONA**
- ✅ Cualquier estado → Cualquier estado

### **Actividades Registradas Automáticamente:**
- 🔄 `"Estado cambiado de RESUELTO a EN_PROGRESO"`
- 🔄 `"Ticket reabierto"` (cuando va a ABIERTO)
- ⏱️ `"Inicio de atención del ticket"` (cuando va a EN_PROGRESO)
- ✅ `"Ticket resuelto - Duración: X minutos"`
- 🔒 `"Ticket cerrado"`

## **📡 URLs Funcionales**

### **Para actualizar tickets:**
```
PUT http://localhost:8080/api/soporte/tickets/54
PUT http://localhost:8080/api/soporte/ticketsoporte/54  ← Compatibilidad
```

### **Para cambiar solo estado:**
```
PUT http://localhost:8080/api/soporte/tickets/54/estado
Body: { "estado": "EN_PROGRESO" }
```

### **Para ver actividades:**
```
GET http://localhost:8080/api/soporte/tickets/54/actividades
GET http://localhost:8080/api/soporte/ticketsoporte/54/actividades  ← Compatibilidad
```

## **🔐 Matriz de Autorización por Rol**

### **👑 Roles con Permisos Completos:**
- **ADMIN** - Control total del sistema
- **SOPORTE_N1** - Gestión de tickets nivel 1
- **SOPORTE_N2** - Gestión de tickets nivel 2  
- **GESTOR_CAMBIOS** - Gestión de cambios
- **CAB_MEMBER** - Miembro del comité de cambios
- **PROJECT_MANAGER** - Gestión de proyectos

### **👤 Roles con Permisos Limitados:**
- **USER** - Solo puede:
  - ✅ Crear tickets
  - ✅ Ver sus propios tickets
  - ✅ Actualizar descripción/solución de sus tickets
  - ✅ Agregar comentarios
  - ❌ **NO** puede cambiar estados
  - ❌ **NO** puede cambiar prioridades
  - ❌ **NO** puede asignar responsables

- **GUEST** - Solo puede:
  - ✅ Ver tickets (limitado)
  - ✅ Actualizar descripción de sus tickets
  - ❌ **NO** puede crear tickets nuevos
  - ❌ **NO** puede cambiar estados
  - ❌ **NO** puede cambiar prioridades

## **🎉 Resultado**

**Seguridad Granular Implementada:**
- ✅ **Roles de soporte** pueden cambiar cualquier estado a cualquier estado
- ✅ **Usuarios finales** solo pueden actualizar contenido, no gestión
- ✅ **Validación de propietario** - USER/GUEST solo editan sus propios tickets
- ✅ **Sistema registra automáticamente** todas las acciones con usuario responsable
- ✅ **Flexibilidad total** para personal de soporte
- ✅ **Protección adecuada** contra cambios no autorizados

**¡Autorización ITIL profesional implementada!** 🚀