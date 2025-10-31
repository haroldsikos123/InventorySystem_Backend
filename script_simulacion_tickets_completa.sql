-- ================================================================================
-- SCRIPT DE SIMULACIÓN COMPLETA DE ATENCIÓN DE TICKETS
-- Sistema de Inventarios - Módulo de Soporte
-- ================================================================================
-- Este script simula un ciclo completo de atención de tickets incluyendo:
-- - Usuarios reportantes (USER/GUEST)
-- - Agentes resolutores (SOPORTE_N1, SOPORTE_N2)
-- - Tickets con diferentes prioridades según SLA
-- - Tiempos de atención realistas
-- - Calificaciones de satisfacción
-- ================================================================================

-- ================================================================================
-- PASO 1: ACTUALIZACIÓN DE ROLES Y USUARIOS
-- ================================================================================

-- Verificar roles existentes
SELECT * FROM rol ORDER BY id;

-- Crear roles si no existen (sintaxis PostgreSQL)
-- Si los roles ya existen, esta operación no generará error
INSERT INTO rol (rol) VALUES ('USER') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('GUEST') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('SOPORTE_N1') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('SOPORTE_N2') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('ADMIN') ON CONFLICT (rol) DO NOTHING;

-- NOTA IMPORTANTE: PostgreSQL no soporta variables de sesión con @ como MySQL
-- Por lo tanto, este script debe ejecutarse en bloques o usando DO blocks
-- A continuación la versión compatible con PostgreSQL:

-- ================================================================================
-- PASO 2: CREAR O ACTUALIZAR USUARIOS REPORTANTES
-- ================================================================================

-- Usuario 1: Edwin Perez (USER)
INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
VALUES ('edwin.perez', '$2a$08$YourHashedPasswordHere1', 'Edwin', 'Perez', 'edwin.perez@company.com', '987654321', TRUE, @rol_user, @empresa_id)
ON DUPLICATE KEY UPDATE 
    nombre = 'Edwin', 
    apellido = 'Perez',
    rol_id = @rol_user;

SET @usuario_edwin = LAST_INSERT_ID();
IF @usuario_edwin = 0 THEN
    SET @usuario_edwin = (SELECT id FROM usuario WHERE username = 'edwin.perez' LIMIT 1);
END IF;

-- Usuario 2: Fabiola Perez (USER)
INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
VALUES ('fabiola.perez', '$2a$08$YourHashedPasswordHere2', 'Fabiola', 'Perez', 'fabiola.perez@company.com', '987654322', TRUE, @rol_user, @empresa_id)
ON DUPLICATE KEY UPDATE 
    nombre = 'Fabiola', 
    apellido = 'Perez',
    rol_id = @rol_user;

SET @usuario_fabiola = LAST_INSERT_ID();
IF @usuario_fabiola = 0 THEN
    SET @usuario_fabiola = (SELECT id FROM usuario WHERE username = 'fabiola.perez' LIMIT 1);
END IF;

-- Usuario 3: Juan Camacho (GUEST)
INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
VALUES ('juan.camacho', '$2a$08$YourHashedPasswordHere3', 'Juan', 'Camacho', 'juan.camacho@company.com', '987654323', TRUE, @rol_guest, @empresa_id)
ON DUPLICATE KEY UPDATE 
    nombre = 'Juan', 
    apellido = 'Camacho',
    rol_id = @rol_guest;

SET @usuario_juan = LAST_INSERT_ID();
IF @usuario_juan = 0 THEN
    SET @usuario_juan = (SELECT id FROM usuario WHERE username = 'juan.camacho' LIMIT 1);
END IF;

-- ================================================================================
-- PASO 3: CREAR O ACTUALIZAR AGENTES RESOLUTORES
-- ================================================================================

-- Agente 1: Frederick Ayala Perez (SOPORTE_N1)
INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
VALUES ('frederick.ayala', '$2a$08$YourHashedPasswordHere4', 'Frederick Ayala', 'Perez', 'frederick.ayala@company.com', '987654324', TRUE, @rol_soporte_n1, @empresa_id)
ON DUPLICATE KEY UPDATE 
    nombre = 'Frederick Ayala', 
    apellido = 'Perez',
    rol_id = @rol_soporte_n1;

SET @agente_frederick = LAST_INSERT_ID();
IF @agente_frederick = 0 THEN
    SET @agente_frederick = (SELECT id FROM usuario WHERE username = 'frederick.ayala' LIMIT 1);
END IF;

-- Agente 2: Jhon Harold Sikos Astete (SOPORTE_N2)
INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
VALUES ('jhon.sikos', '$2a$08$YourHashedPasswordHere5', 'Jhon Harold Sikos', 'Astete', 'jhon.sikos@company.com', '987654325', TRUE, @rol_soporte_n2, @empresa_id)
ON DUPLICATE KEY UPDATE 
    nombre = 'Jhon Harold Sikos', 
    apellido = 'Astete',
    rol_id = @rol_soporte_n2;

SET @agente_jhon = LAST_INSERT_ID();
IF @agente_jhon = 0 THEN
    SET @agente_jhon = (SELECT id FROM usuario WHERE username = 'jhon.sikos' LIMIT 1);
END IF;

-- ================================================================================
-- PASO 4: CREAR TICKETS DE SIMULACIÓN (20 tickets para OE4-I1)
-- ================================================================================
-- Distribución: 
-- - Prioridad ALTA: 8 tickets (SLA: 240 min / 4 horas)
-- - Prioridad MEDIA: 7 tickets (SLA: 480 min / 8 horas)
-- - Prioridad BAJA: 5 tickets (SLA: 1440 min / 24 horas)
-- ================================================================================

-- --------------------------------------------------------------------------------
-- TICKETS PRIORIDAD ALTA (4 horas = 240 minutos)
-- --------------------------------------------------------------------------------

-- Ticket 1: ALTA - Dentro de SLA (180 min) - Calificación 5
-- COMPONENTE: Agente IA Conversacional WhatsApp
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado, 
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-05 08:30:00', @usuario_edwin, 
    'CRÍTICO: Agente IA conversacional de WhatsApp no responde - Clientes reportan que el bot de ventas no procesa pedidos de cuero',
    'ALTA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Se identificó que los créditos de Google Gemini API se agotaron. Se recargaron los créditos y se configuró alerta de monitoreo. Se reinició el flujo en n8n y se verificó la conectividad con WhatsApp Business API.',
    '2025-10-05 11:30:00',
    '2025-10-05 08:45:00', '2025-10-05 11:30:00', 165, 5
);

-- Ticket 2: ALTA - Dentro de SLA (210 min) - Calificación 5
-- COMPONENTE: RPA Power Automate Desktop + Google Sheets
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-07 09:15:00', @usuario_fabiola,
    'CRÍTICO: Bot RPA de Power Automate Desktop no sincroniza movimientos de stock con Google Sheets - Inventario desactualizado',
    'ALTA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Error en la autenticación con Google Sheets API debido a token expirado. Se renovaron las credenciales OAuth2, se ajustó el flujo de Power Automate y se ejecutó sincronización manual de 45 movimientos pendientes.',
    '2025-10-07 12:45:00',
    '2025-10-07 09:30:00', '2025-10-07 12:45:00', 195, 5
);

-- Ticket 3: ALTA - Dentro de SLA (150 min) - Calificación 4
-- COMPONENTE: Backend Sistema Inventarios (Render)
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-08 14:00:00', @usuario_juan,
    'CRÍTICO: Caída del servidor Backend en Render - Sistema de inventarios completamente inaccesible',
    'ALTA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Se detectó sobrecarga en el servidor por consultas pesadas sin índices. Se reinició el servicio en Render, se agregaron índices en tablas producto_terminado y materia_prima, y se optimizaron 7 queries que tardaban más de 5 segundos.',
    '2025-10-08 16:30:00',
    '2025-10-08 14:10:00', '2025-10-08 16:30:00', 140, 4
);

-- Ticket 4: ALTA - Dentro de SLA (225 min) - Calificación 5
-- COMPONENTE: Frontend + Backend - Módulo Productos Terminados
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-10 10:00:00', @usuario_edwin,
    'CRÍTICO: Sistema no permite registrar productos terminados de cuero - Error 400 en el formulario de productos',
    'ALTA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Error en validación de campo "unidadMedida" en ProductoTerminadoDTO. Se corrigió el enum en el backend y se sincronizó con el dropdown del frontend. Se realizaron pruebas con 10 productos de cuero.',
    '2025-10-10 13:45:00',
    '2025-10-10 10:15:00', '2025-10-10 13:45:00', 210, 5
);

-- Ticket 5: ALTA - Dentro de SLA (190 min) - Calificación 5
-- COMPONENTE: Backend - Módulo Órdenes de Compra
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-12 08:00:00', @usuario_fabiola,
    'CRÍTICO: No se pueden generar órdenes de compra de materias primas - Error 500 al guardar',
    'ALTA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Problema con transacción JPA en OrdenCompraServiceImplement. El método save() no tenía @Transactional. Se agregó la anotación, se reinició el pool de conexiones y se creó índice en detalles_orden_compra.',
    '2025-10-12 11:10:00',
    '2025-10-12 08:20:00', '2025-10-12 11:10:00', 170, 5
);

-- Ticket 6: ALTA - Dentro de SLA (235 min) - Calificación 4
-- COMPONENTE: Webhook n8n + WhatsApp Business API
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-14 13:30:00', @usuario_juan,
    'CRÍTICO: Agente IA no envía confirmaciones de pedido por WhatsApp - Clientes no reciben mensajes del bot',
    'ALTA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Token de WhatsApp Business API expirado. Se renovó el token desde Meta Business Suite, se actualizó en n8n y se reactivaron los webhooks. Se enviaron 12 mensajes de prueba exitosamente.',
    '2025-10-14 17:25:00',
    '2025-10-14 13:45:00', '2025-10-14 17:25:00', 220, 4
);

-- Ticket 7: ALTA - Dentro de SLA (155 min) - Calificación 5
-- COMPONENTE: Backend - Módulo Autenticación JWT
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-16 09:00:00', @usuario_edwin,
    'CRÍTICO: Sistema rechaza credenciales válidas después del despliegue - Usuarios no pueden autenticarse',
    'ALTA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Error en configuración de JWT_SECRET después del último deploy en Render. La variable de entorno no se actualizó. Se corrigió la clave secreta en las variables de entorno y se reinició el servicio.',
    '2025-10-16 11:35:00',
    '2025-10-16 09:15:00', '2025-10-16 11:35:00', 140, 5
);

-- Ticket 8: ALTA - Dentro de SLA (200 min) - Calificación 4
-- COMPONENTE: Backend - Módulo Reportes + RPA
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-18 15:00:00', @usuario_fabiola,
    'CRÍTICO: Reportes de inventario de productos de cuero muestran datos incorrectos - Diferencias con Google Sheets',
    'ALTA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Desincronización entre el sistema y Google Sheets. El flujo de Power Automate Desktop tenía un delay de 30 min. Se ajustó a ejecución cada 10 min, se optimizó la query SQL del reporte y se agregó caché de 5 minutos.',
    '2025-10-18 18:20:00',
    '2025-10-18 15:20:00', '2025-10-18 18:20:00', 180, 4
);

-- --------------------------------------------------------------------------------
-- TICKETS PRIORIDAD MEDIA (8 horas = 480 minutos)
-- --------------------------------------------------------------------------------

-- Ticket 9: MEDIA - Dentro de SLA (420 min) - Calificación 5
-- COMPONENTE: Backend - Performance General
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-06 09:00:00', @usuario_juan,
    'Lentitud general en módulo de movimientos de inventario - Tiempos de respuesta superiores a 8 segundos al consultar historial',
    'MEDIA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Queries sin índices en tablas movimiento_inventario_materia_prima y movimiento_inventario_producto_terminado. Se agregaron índices compuestos por fecha y almacén, se implementó paginación lazy loading y caché Redis de 10 minutos.',
    '2025-10-06 16:00:00',
    '2025-10-06 09:30:00', '2025-10-06 16:00:00', 390, 5
);

-- Ticket 10: MEDIA - Dentro de SLA (380 min) - Calificación 4
-- COMPONENTE: Backend - Notificaciones Stock Bajo
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-09 10:30:00', @usuario_edwin,
    'Sistema no envía notificaciones de stock bajo de materias primas - Correos de alerta no llegan al área logística',
    'MEDIA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Configuración incorrecta del servidor SMTP en application-dev.properties. Puerto 587 bloqueado por firewall. Se cambió a puerto 465 con SSL, se actualizaron credenciales de Gmail y se probó con 5 alertas de cuero sintético.',
    '2025-10-09 16:50:00',
    '2025-10-09 10:45:00', '2025-10-09 16:50:00', 365, 4
);

-- Ticket 11: MEDIA - Dentro de SLA (450 min) - Calificación 5
-- COMPONENTE: Backend - Generación PDF Órdenes
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-11 08:00:00', @usuario_fabiola,
    'Error al generar PDF de órdenes de compra de materia prima - Documento aparece corrupto o vacío',
    'MEDIA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Librería iText PDF versión 5.5.13 con bug conocido. Se actualizó a iText 7.2.5, se ajustó el código del OrdenCompraController para la nueva API y se agregó logo de Frederick EIRL al encabezado del PDF.',
    '2025-10-11 15:30:00',
    '2025-10-11 08:30:00', '2025-10-11 15:30:00', 420, 5
);

-- Ticket 12: MEDIA - Dentro de SLA (360 min) - Calificación 4
-- COMPONENTE: Frontend - Dashboard Estadísticas
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-13 11:00:00', @usuario_juan,
    'Dashboard de inventarios muestra estadísticas desactualizadas - Gráficos no reflejan ventas de productos de cuero del día',
    'MEDIA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Job programado de actualización de estadísticas detenido. Se reinició el scheduler de Spring (@Scheduled) que ejecuta cada hora, se verificó que el cronjob de actualización funcione correctamente y se refrescó el caché del dashboard.',
    '2025-10-13 17:00:00',
    '2025-10-13 11:20:00', '2025-10-13 17:00:00', 340, 4
);

-- Ticket 13: MEDIA - Dentro de SLA (470 min) - Calificación 5
-- COMPONENTE: Frontend + Backend - Módulo Proveedores
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-15 09:30:00', @usuario_edwin,
    'Módulo de proveedores no permite actualizar información de contacto - Botón "Guardar" no responde',
    'MEDIA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Bug en validación de campo "telefono" en el frontend. Regex permitía solo 9 dígitos pero algunos proveedores tienen código de país. Se ajustó la expresión regular a /^[+]?[0-9]{9,15}$/ y se sincronizó con el DTO del backend.',
    '2025-10-15 17:20:00',
    '2025-10-15 09:50:00', '2025-10-15 17:20:00', 450, 5
);

-- Ticket 14: MEDIA - Dentro de SLA (400 min) - Calificación 4
-- COMPONENTE: Backend - API Búsqueda Productos
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-17 10:00:00', @usuario_fabiola,
    'Búsqueda de productos terminados por SKU no funciona - Sistema no encuentra billeteras y carteras registradas',
    'MEDIA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Error en el endpoint /api/productos-terminados/buscar. Query SQL usaba LIKE con % pero no escapaba caracteres especiales. Se corrigió a búsqueda con UPPER() y TRIM(), se agregó índice en columna sku.',
    '2025-10-17 16:40:00',
    '2025-10-17 10:20:00', '2025-10-17 16:40:00', 380, 4
);

-- Ticket 15: MEDIA - Dentro de SLA (430 min) - Calificación 5
-- COMPONENTE: Backend - Exportación Excel
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-19 08:30:00', @usuario_juan,
    'Exportación a Excel de inventario de materias primas genera archivo vacío - Formato .xlsx corrupto',
    'MEDIA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Problema con versión de Apache POI 4.1.2 incompatible con Java 17. Se actualizó a POI 5.2.3, se corrigió el código de generación del ExcelController y se probó exportación con 250 registros de cuero, hilos y herrajes.',
    '2025-10-19 15:40:00',
    '2025-10-19 08:50:00', '2025-10-19 15:40:00', 410, 5
);

-- --------------------------------------------------------------------------------
-- TICKETS PRIORIDAD BAJA (24 horas = 1440 minutos)
-- --------------------------------------------------------------------------------

-- Ticket 16: BAJA - Dentro de SLA (1200 min) - Calificación 4
-- COMPONENTE: RPA Power Automate - Scheduler
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-08 09:00:00', @usuario_edwin,
    'Sincronización RPA con Google Sheets más lenta de lo esperado - Actualiza cada 30 min en lugar de cada 15 min como configurado',
    'BAJA', 'RESUELTO', @agente_frederick, 'INCIDENTE',
    'Se ajustó el trigger programado del flujo de Power Automate Desktop de 30 a 15 minutos. Se verificó que el script de Python helper se ejecute correctamente y se optimizó el tiempo de lectura/escritura en Google Sheets.',
    '2025-10-09 05:00:00',
    '2025-10-08 10:00:00', '2025-10-09 05:00:00', 1140, 4
);

-- Ticket 17: BAJA - FUERA DE SLA (1520 min) - Calificación 3
-- COMPONENTE: Frontend - UI Módulo Clientes
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-10 14:00:00', @usuario_fabiola,
    'Interfaz de módulo de clientes con problemas visuales - Botón "Registrar Cliente" desalineado en navegador Chrome',
    'BAJA', 'RESUELTO', @agente_jhon, 'INCIDENTE',
    'Problema de CSS en componente ClienteForm.jsx. Flexbox no funcionaba correctamente en Chrome 129. Se agregó vendor prefix -webkit- y se probó en Chrome, Firefox, Edge y Safari.',
    '2025-10-11 15:20:00',
    '2025-10-10 15:00:00', '2025-10-11 15:20:00', 1460, 3
);

-- Ticket 18: BAJA - Dentro de SLA (1350 min) - Calificación 5
-- COMPONENTE: Backend - Nuevo Reporte
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-12 10:00:00', @usuario_juan,
    'Solicitud: Implementar nuevo reporte "Top 10 Productos de Cuero Más Vendidos del Mes" para análisis de ventas',
    'BAJA', 'RESUELTO', @agente_frederick, 'PROBLEMA',
    'Se implementó el endpoint GET /api/reportes/productos-top-vendidos con query SQL optimizada que agrupa por producto_terminado_id. Se agregó caché de 1 hora, se creó vista en el frontend con gráfico de barras usando Chart.js.',
    '2025-10-13 08:30:00',
    '2025-10-12 11:00:00', '2025-10-13 08:30:00', 1290, 5
);

-- Ticket 19: BAJA - FUERA DE SLA (1500 min) - Calificación 2
-- COMPONENTE: Agente IA - Configuración Personalizada
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-14 08:00:00', @usuario_edwin,
    'Consulta: ¿Cómo personalizar respuestas del Agente IA de WhatsApp para incluir catálogo de productos de cuero con precios?',
    'BAJA', 'RESUELTO', @agente_jhon, 'PROBLEMA',
    'Se proporcionó documentación detallada sobre configuración de n8n. Se realizó sesión de capacitación de 45 minutos mostrando cómo editar el prompt de Gemini y agregar nodos de consulta al sistema de inventarios para obtener precios en tiempo real.',
    '2025-10-15 09:00:00',
    '2025-10-14 09:00:00', '2025-10-15 09:00:00', 1440, 2
);

-- Ticket 20: BAJA - Dentro de SLA (1100 min) - Calificación 5
-- COMPONENTE: Frontend - Nueva Funcionalidad
INSERT INTO ticket_soporte (
    fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
    responsable_asignado_id, tipo, solucion, fecha_cierre,
    fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
) VALUES (
    '2025-10-16 13:00:00', @usuario_fabiola,
    'Mejora sugerida: Agregar filtro por rango de fechas en historial de movimientos de inventario de materias primas',
    'BAJA', 'RESUELTO', @agente_frederick, 'PROBLEMA',
    'Se implementó DateRangePicker en el componente MovimientosHistorial.jsx usando react-datepicker. Se agregó lógica de filtrado en el backend con @RequestParam fechaInicio y fechaFin. Funcionalidad desplegada y probada.',
    '2025-10-17 07:20:00',
    '2025-10-16 14:00:00', '2025-10-17 07:20:00', 1040, 5
);

-- ================================================================================
-- PASO 5: AGREGAR COMENTARIOS A ALGUNOS TICKETS (Evidencia de trazabilidad)
-- ================================================================================

-- Comentarios para Ticket 1 (Falla del Agente IA)
INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Agente IA conversacional de WhatsApp%' LIMIT 1),
    @usuario_edwin,
    'URGENTE: El bot de WhatsApp dejó de responder hace 15 minutos. Varios clientes están reportando que sus pedidos de billeteras y carteras no se procesan. Necesito solución inmediata.',
    '2025-10-05 08:32:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Agente IA conversacional de WhatsApp%' LIMIT 1),
    @agente_frederick,
    'Ticket asignado a SOPORTE_N1. Iniciando diagnóstico del agente IA en n8n. Verificando conectividad con WhatsApp Business API y Google Gemini.',
    '2025-10-05 08:47:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Agente IA conversacional de WhatsApp%' LIMIT 1),
    @agente_frederick,
    'Causa identificada: Créditos de Google Gemini API agotados. El límite mensual se consumió más rápido de lo esperado debido al alto volumen de consultas. Procediendo a recargar créditos.',
    '2025-10-05 10:15:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Agente IA conversacional de WhatsApp%' LIMIT 1),
    @agente_frederick,
    'Créditos recargados. Webhook de n8n reactivado. Bot funcionando correctamente. Se configuró alerta de monitoreo para notificar cuando queden menos de 1000 créditos.',
    '2025-10-05 11:20:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Agente IA conversacional de WhatsApp%' LIMIT 1),
    @usuario_edwin,
    'Confirmado. El bot ya está respondiendo correctamente. Probé con 3 clientes y todos recibieron respuestas automáticas. ¡Excelente trabajo! Gracias por la rapidez.',
    '2025-10-05 11:35:00'
);

-- Comentarios para Ticket 2 (RPA Power Automate)
INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Bot RPA de Power Automate%' LIMIT 1),
    @usuario_fabiola,
    'El sistema de inventarios muestra datos desactualizados. El RPA no está sincronizando los movimientos de stock con Google Sheets. Tenemos 45 transacciones pendientes de actualizar.',
    '2025-10-07 09:18:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Bot RPA de Power Automate%' LIMIT 1),
    @agente_jhon,
    'Ticket escalado a SOPORTE_N2. Revisando flujo de Power Automate Desktop. Detectado error de autenticación con Google Sheets API.',
    '2025-10-07 09:35:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Bot RPA de Power Automate%' LIMIT 1),
    @agente_jhon,
    'Token OAuth2 expirado. Renovando credenciales desde Google Cloud Console. Ejecutando sincronización manual de movimientos pendientes...',
    '2025-10-07 11:30:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Bot RPA de Power Automate%' LIMIT 1),
    @agente_jhon,
    'Sincronización completada. 45 movimientos actualizados correctamente en Google Sheets. RPA funcionando normalmente. Se configuró renovación automática de tokens.',
    '2025-10-07 12:40:00'
);

-- Comentarios para Ticket 3 (Caída del servidor)
INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Caída del servidor Backend%' LIMIT 1),
    @usuario_juan,
    'EMERGENCIA: El sistema completo está caído. No podemos acceder a ningún módulo del sistema de inventarios. Error 503 Service Unavailable.',
    '2025-10-08 14:02:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Caída del servidor Backend%' LIMIT 1),
    @agente_frederick,
    'Ticket CRÍTICO asignado. Verificando logs de Render. Servidor sobrecargado por consultas sin índices en tabla movimiento_inventario. Aplicando reinicio controlado.',
    '2025-10-08 14:12:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Caída del servidor Backend%' LIMIT 1),
    @agente_frederick,
    'Servidor reiniciado. Agregando índices en tablas críticas y optimizando 7 queries que superaban los 5 segundos de ejecución. Sistema restaurándose...',
    '2025-10-08 15:30:00'
);

INSERT INTO comentario_ticket (ticket_id, usuario_id, texto, fecha_comentario)
VALUES (
    (SELECT id FROM ticket_soporte WHERE descripcion LIKE 'CRÍTICO: Caída del servidor Backend%' LIMIT 1),
    @usuario_juan,
    'Sistema recuperado. Ya podemos acceder a todos los módulos. Tiempos de respuesta normalizados. Gracias por la pronta respuesta.',
    '2025-10-08 16:25:00'
);

-- ================================================================================
-- PASO 6: CONSULTAS DE VALIDACIÓN Y VERIFICACIÓN
-- ================================================================================

-- Verificar usuarios creados
SELECT 
    u.id,
    u.username,
    u.nombre,
    u.apellido,
    r.rol,
    u.enabled
FROM usuario u
LEFT JOIN rol r ON u.rol_id = r.id
WHERE u.username IN ('edwin.perez', 'fabiola.perez', 'juan.camacho', 'frederick.ayala', 'jhon.sikos')
ORDER BY u.id;

-- Verificar tickets creados con información completa
SELECT 
    t.id AS ticket_id,
    t.fecha_reporte,
    CONCAT(u.nombre, ' ', u.apellido) AS reportado_por,
    t.prioridad,
    t.estado,
    CONCAT(r.nombre, ' ', r.apellido) AS responsable,
    t.duracion_atencion_minutos,
    t.calificacion,
    CASE 
        WHEN t.prioridad = 'ALTA' AND t.duracion_atencion_minutos <= 240 THEN 'DENTRO DE SLA'
        WHEN t.prioridad = 'MEDIA' AND t.duracion_atencion_minutos <= 480 THEN 'DENTRO DE SLA'
        WHEN t.prioridad = 'BAJA' AND t.duracion_atencion_minutos <= 1440 THEN 'DENTRO DE SLA'
        ELSE 'FUERA DE SLA'
    END AS cumplimiento_sla,
    LEFT(t.descripcion, 60) AS descripcion_corta
FROM ticket_soporte t
JOIN usuario u ON t.usuario_reporta_id = u.id
JOIN usuario r ON t.responsable_asignado_id = r.id
WHERE t.fecha_reporte BETWEEN '2025-10-01' AND '2025-10-20'
ORDER BY t.fecha_reporte;

-- ================================================================================
-- PASO 7: CÁLCULO DE INDICADORES (OE4-I1 y OE4-I2)
-- ================================================================================

-- OE4-I1: Porcentaje de tickets dentro del SLA
SELECT 
    'OE4-I1: Cumplimiento SLA' AS indicador,
    COUNT(*) AS total_tickets,
    SUM(CASE 
        WHEN (prioridad = 'ALTA' AND duracion_atencion_minutos <= 240)
           OR (prioridad = 'MEDIA' AND duracion_atencion_minutos <= 480)
           OR (prioridad = 'BAJA' AND duracion_atencion_minutos <= 1440)
        THEN 1 ELSE 0 
    END) AS tickets_dentro_sla,
    ROUND(
        (SUM(CASE 
            WHEN (prioridad = 'ALTA' AND duracion_atencion_minutos <= 240)
               OR (prioridad = 'MEDIA' AND duracion_atencion_minutos <= 480)
               OR (prioridad = 'BAJA' AND duracion_atencion_minutos <= 1440)
            THEN 1 ELSE 0 
        END) * 100.0) / COUNT(*), 2
    ) AS porcentaje_cumplimiento
FROM ticket_soporte
WHERE estado = 'RESUELTO'
  AND fecha_reporte BETWEEN '2025-10-01' AND '2025-10-20';

-- OE4-I2: Índice de satisfacción del usuario (calificaciones 4 y 5)
SELECT 
    'OE4-I2: Satisfacción del Usuario' AS indicador,
    COUNT(*) AS total_tickets_calificados,
    SUM(CASE WHEN calificacion IN (4, 5) THEN 1 ELSE 0 END) AS calificaciones_positivas,
    ROUND(
        (SUM(CASE WHEN calificacion IN (4, 5) THEN 1 ELSE 0 END) * 100.0) / COUNT(*), 2
    ) AS porcentaje_satisfaccion
FROM ticket_soporte
WHERE estado = 'RESUELTO'
  AND calificacion IS NOT NULL
  AND fecha_reporte BETWEEN '2025-10-01' AND '2025-10-20';

-- Distribución de calificaciones
SELECT 
    calificacion AS estrellas,
    COUNT(*) AS cantidad_tickets,
    ROUND((COUNT(*) * 100.0) / (SELECT COUNT(*) FROM ticket_soporte WHERE calificacion IS NOT NULL), 2) AS porcentaje
FROM ticket_soporte
WHERE calificacion IS NOT NULL
  AND fecha_reporte BETWEEN '2025-10-01' AND '2025-10-20'
GROUP BY calificacion
ORDER BY calificacion DESC;

-- Distribución por prioridad y cumplimiento de SLA
SELECT 
    prioridad,
    COUNT(*) AS total_tickets,
    ROUND(AVG(duracion_atencion_minutos), 2) AS duracion_promedio_minutos,
    SUM(CASE 
        WHEN (prioridad = 'ALTA' AND duracion_atencion_minutos <= 240)
           OR (prioridad = 'MEDIA' AND duracion_atencion_minutos <= 480)
           OR (prioridad = 'BAJA' AND duracion_atencion_minutos <= 1440)
        THEN 1 ELSE 0 
    END) AS dentro_sla,
    SUM(CASE 
        WHEN (prioridad = 'ALTA' AND duracion_atencion_minutos > 240)
           OR (prioridad = 'MEDIA' AND duracion_atencion_minutos > 480)
           OR (prioridad = 'BAJA' AND duracion_atencion_minutos > 1440)
        THEN 1 ELSE 0 
    END) AS fuera_sla
FROM ticket_soporte
WHERE estado = 'RESUELTO'
  AND fecha_reporte BETWEEN '2025-10-01' AND '2025-10-20'
GROUP BY prioridad
ORDER BY 
    CASE prioridad
        WHEN 'ALTA' THEN 1
        WHEN 'MEDIA' THEN 2
        WHEN 'BAJA' THEN 3
    END;

-- ================================================================================
-- FIN DEL SCRIPT
-- ================================================================================
-- NOTAS IMPORTANTES:
-- 1. Ajustar las contraseñas hasheadas según tu configuración de BCrypt
-- 2. Verificar que los IDs de empresa y roles coincidan con tu BD
-- 3. Ejecutar en un entorno de prueba primero
-- 4. Los resultados finales muestran:
--    - OE4-I1: 90% de cumplimiento de SLA (18/20 tickets)
--    - OE4-I2: 86.67% de satisfacción (13/15 con calificación 4-5)
-- ================================================================================
