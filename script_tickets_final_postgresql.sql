-- ================================================================================
-- SCRIPT DE SIMULACIÓN COMPLETA DE ATENCIÓN DE TICKETS (PostgreSQL - FINAL)
-- Sistema de Inventarios - Módulo de Soporte
-- ================================================================================
-- ADAPTADO 100% A LA ESTRUCTURA DEL BACKEND
-- - Nombres de columnas exactos según entidades JPA
-- - Tipos de datos correctos (telefono como BIGINT)
-- - Enums: ALTA/MEDIA/BAJA, RESUELTO, INCIDENTE/PROBLEMA
-- - Constraint UNIQUE en rol(rol)
-- ================================================================================

-- ================================================================================
-- REQUISITO PREVIO: Agregar constraint UNIQUE en tabla rol (ejecutar solo una vez)
-- ================================================================================
ALTER TABLE rol ADD CONSTRAINT uk_rol_nombre UNIQUE (rol);

-- ================================================================================
-- PASO 1: CREAR ROLES
-- ================================================================================

INSERT INTO rol (rol) VALUES ('USER') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('GUEST') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('SOPORTE_N1') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('SOPORTE_N2') ON CONFLICT (rol) DO NOTHING;
INSERT INTO rol (rol) VALUES ('ADMIN') ON CONFLICT (rol) DO NOTHING;

-- Verificar roles creados
SELECT * FROM rol ORDER BY id;

-- ================================================================================
-- PASO 2: CREAR USUARIOS Y TICKETS CON DO BLOCK
-- ================================================================================

DO $$
DECLARE
    v_rol_user INTEGER;
    v_rol_guest INTEGER;
    v_rol_soporte_n1 INTEGER;
    v_rol_soporte_n2 INTEGER;
    v_empresa_id INTEGER := 1; -- AJUSTAR ESTE VALOR SEGÚN TU BASE DE DATOS
    
    v_usuario_edwin INTEGER;
    v_usuario_fabiola INTEGER;
    v_usuario_juan INTEGER;
    v_agente_frederick INTEGER;
    v_agente_jhon INTEGER;
BEGIN
    -- Obtener IDs de roles
    SELECT id INTO v_rol_user FROM rol WHERE rol = 'USER' LIMIT 1;
    SELECT id INTO v_rol_guest FROM rol WHERE rol = 'GUEST' LIMIT 1;
    SELECT id INTO v_rol_soporte_n1 FROM rol WHERE rol = 'SOPORTE_N1' LIMIT 1;
    SELECT id INTO v_rol_soporte_n2 FROM rol WHERE rol = 'SOPORTE_N2' LIMIT 1;

    -- ============================================================================
    -- CREAR USUARIOS REPORTANTES
    -- ============================================================================
    
    -- Usuario 1: Edwin Perez (USER)
    INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
    VALUES ('edwin.perez', '$2a$10$vI8aWBnW3fID.vQ4/yr1K.JhXnQXO5uQQf9V1n0fFLGp0Y7FKFwEm', 'Edwin', 'Perez', 
            'edwin.perez@frederick.com', 987654321, TRUE, v_rol_user, v_empresa_id)
    ON CONFLICT (username) DO UPDATE SET
        nombre = 'Edwin',
        apellido = 'Perez',
        rol_id = v_rol_user
    RETURNING id INTO v_usuario_edwin;
    
    IF v_usuario_edwin IS NULL THEN
        SELECT id INTO v_usuario_edwin FROM usuario WHERE username = 'edwin.perez' LIMIT 1;
    END IF;

    -- Usuario 2: Fabiola Perez (USER)
    INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
    VALUES ('fabiola.perez', '$2a$10$vI8aWBnW3fID.vQ4/yr1K.JhXnQXO5uQQf9V1n0fFLGp0Y7FKFwEm', 'Fabiola', 'Perez',
            'fabiola.perez@frederick.com', 987654322, TRUE, v_rol_user, v_empresa_id)
    ON CONFLICT (username) DO UPDATE SET
        nombre = 'Fabiola',
        apellido = 'Perez',
        rol_id = v_rol_user
    RETURNING id INTO v_usuario_fabiola;
    
    IF v_usuario_fabiola IS NULL THEN
        SELECT id INTO v_usuario_fabiola FROM usuario WHERE username = 'fabiola.perez' LIMIT 1;
    END IF;

    -- Usuario 3: Juan Camacho (GUEST)
    INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
    VALUES ('juan.camacho', '$2a$10$vI8aWBnW3fID.vQ4/yr1K.JhXnQXO5uQQf9V1n0fFLGp0Y7FKFwEm', 'Juan', 'Camacho',
            'juan.camacho@frederick.com', 987654323, TRUE, v_rol_guest, v_empresa_id)
    ON CONFLICT (username) DO UPDATE SET
        nombre = 'Juan',
        apellido = 'Camacho',
        rol_id = v_rol_guest
    RETURNING id INTO v_usuario_juan;
    
    IF v_usuario_juan IS NULL THEN
        SELECT id INTO v_usuario_juan FROM usuario WHERE username = 'juan.camacho' LIMIT 1;
    END IF;

    -- ============================================================================
    -- CREAR AGENTES RESOLUTORES
    -- ============================================================================

    -- Agente 1: Frederick Ayala Perez (SOPORTE_N1)
    INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
    VALUES ('frederick.ayala', '$2a$10$vI8aWBnW3fID.vQ4/yr1K.JhXnQXO5uQQf9V1n0fFLGp0Y7FKFwEm', 'Frederick Ayala', 'Perez',
            'frederick.ayala@frederick.com', 987654324, TRUE, v_rol_soporte_n1, v_empresa_id)
    ON CONFLICT (username) DO UPDATE SET
        nombre = 'Frederick Ayala',
        apellido = 'Perez',
        rol_id = v_rol_soporte_n1
    RETURNING id INTO v_agente_frederick;
    
    IF v_agente_frederick IS NULL THEN
        SELECT id INTO v_agente_frederick FROM usuario WHERE username = 'frederick.ayala' LIMIT 1;
    END IF;

    -- Agente 2: Jhon Harold Sikos Astete (SOPORTE_N2)
    INSERT INTO usuario (username, password, nombre, apellido, correo, telefono, enabled, rol_id, empresa_id)
    VALUES ('jhon.sikos', '$2a$10$vI8aWBnW3fID.vQ4/yr1K.JhXnQXO5uQQf9V1n0fFLGp0Y7FKFwEm', 'Jhon Harold Sikos', 'Astete',
            'jhon.sikos@frederick.com', 987654325, TRUE, v_rol_soporte_n2, v_empresa_id)
    ON CONFLICT (username) DO UPDATE SET
        nombre = 'Jhon Harold Sikos',
        apellido = 'Astete',
        rol_id = v_rol_soporte_n2
    RETURNING id INTO v_agente_jhon;
    
    IF v_agente_jhon IS NULL THEN
        SELECT id INTO v_agente_jhon FROM usuario WHERE username = 'jhon.sikos' LIMIT 1;
    END IF;

    RAISE NOTICE 'Usuarios creados exitosamente:';
    RAISE NOTICE 'Edwin ID: %, Fabiola ID: %, Juan ID: %', v_usuario_edwin, v_usuario_fabiola, v_usuario_juan;
    RAISE NOTICE 'Frederick ID: %, Jhon ID: %', v_agente_frederick, v_agente_jhon;

    -- ============================================================================
    -- CREAR TICKETS DE SIMULACIÓN (20 tickets)
    -- ============================================================================
    
    -- ------------------------------------------------------------------------
    -- TICKETS PRIORIDAD ALTA (SLA: 240 minutos)
    -- ------------------------------------------------------------------------
    
    -- Ticket 1: Agente IA WhatsApp
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-05 08:30:00', v_usuario_edwin,
        'CRÍTICO: Agente IA conversacional de WhatsApp no responde - Clientes reportan que el bot de ventas no procesa pedidos de cuero',
        'ALTA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Se identificó que los créditos de Google Gemini API se agotaron. Se recargaron los créditos y se configuró alerta de monitoreo. Se reinició el flujo en n8n y se verificó la conectividad con WhatsApp Business API.',
        '2025-10-05 11:30:00',
        '2025-10-05 08:45:00', '2025-10-05 11:30:00', 165, 5
    );

    -- Ticket 2: RPA Power Automate
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-07 09:15:00', v_usuario_fabiola,
        'CRÍTICO: Bot RPA de Power Automate Desktop no sincroniza movimientos de stock con Google Sheets - Inventario desactualizado',
        'ALTA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Error en la autenticación con Google Sheets API debido a token expirado. Se renovaron las credenciales OAuth2, se ajustó el flujo de Power Automate y se ejecutó sincronización manual de 45 movimientos pendientes.',
        '2025-10-07 12:45:00',
        '2025-10-07 09:30:00', '2025-10-07 12:45:00', 195, 5
    );

    -- Ticket 3: Caída Backend
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-08 14:00:00', v_usuario_juan,
        'CRÍTICO: Caída del servidor Backend en Render - Sistema de inventarios completamente inaccesible',
        'ALTA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Se detectó sobrecarga en el servidor por consultas pesadas sin índices. Se reinició el servicio en Render, se agregaron índices en tablas producto_terminado y materia_prima, y se optimizaron 7 queries que tardaban más de 5 segundos.',
        '2025-10-08 16:30:00',
        '2025-10-08 14:10:00', '2025-10-08 16:30:00', 140, 4
    );

    -- Ticket 4: Productos Terminados
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-10 10:00:00', v_usuario_edwin,
        'CRÍTICO: Sistema no permite registrar productos terminados de cuero - Error 400 en el formulario de productos',
        'ALTA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Error en validación de campo "unidadMedida" en ProductoTerminadoDTO. Se corrigió el enum en el backend y se sincronizó con el dropdown del frontend. Se realizaron pruebas con 10 productos de cuero.',
        '2025-10-10 13:45:00',
        '2025-10-10 10:15:00', '2025-10-10 13:45:00', 210, 5
    );

    -- Ticket 5: Órdenes de Compra
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-12 08:00:00', v_usuario_fabiola,
        'CRÍTICO: No se pueden generar órdenes de compra de materias primas - Error 500 al guardar',
        'ALTA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Problema con transacción JPA en OrdenCompraServiceImplement. El método save() no tenía @Transactional. Se agregó la anotación, se reinició el pool de conexiones y se creó índice en detalles_orden_compra.',
        '2025-10-12 11:10:00',
        '2025-10-12 08:20:00', '2025-10-12 11:10:00', 170, 5
    );

    -- Ticket 6: WhatsApp Webhooks
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-14 13:30:00', v_usuario_juan,
        'CRÍTICO: Agente IA no envía confirmaciones de pedido por WhatsApp - Clientes no reciben mensajes del bot',
        'ALTA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Token de WhatsApp Business API expirado. Se renovó el token desde Meta Business Suite, se actualizó en n8n y se reactivaron los webhooks. Se enviaron 12 mensajes de prueba exitosamente.',
        '2025-10-14 17:25:00',
        '2025-10-14 13:45:00', '2025-10-14 17:25:00', 220, 4
    );

    -- Ticket 7: JWT Authentication
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-16 09:00:00', v_usuario_edwin,
        'CRÍTICO: Sistema rechaza credenciales válidas después del despliegue - Usuarios no pueden autenticarse',
        'ALTA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Error en configuración de JWT_SECRET después del último deploy en Render. La variable de entorno no se actualizó. Se corrigió la clave secreta en las variables de entorno y se reinició el servicio.',
        '2025-10-16 11:35:00',
        '2025-10-16 09:15:00', '2025-10-16 11:35:00', 140, 5
    );

    -- Ticket 8: Reportes Desincronizados
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-18 15:00:00', v_usuario_fabiola,
        'CRÍTICO: Reportes de inventario de productos de cuero muestran datos incorrectos - Diferencias con Google Sheets',
        'ALTA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Desincronización entre el sistema y Google Sheets. El flujo de Power Automate Desktop tenía un delay de 30 min. Se ajustó a ejecución cada 10 min, se optimizó la query SQL del reporte y se agregó caché de 5 minutos.',
        '2025-10-18 18:20:00',
        '2025-10-18 15:20:00', '2025-10-18 18:20:00', 180, 4
    );

    -- ------------------------------------------------------------------------
    -- TICKETS PRIORIDAD MEDIA (SLA: 480 minutos)
    -- ------------------------------------------------------------------------

    -- Ticket 9: Performance
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-06 09:00:00', v_usuario_juan,
        'Lentitud general en módulo de movimientos de inventario - Tiempos de respuesta superiores a 8 segundos al consultar historial',
        'MEDIA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Queries sin índices en tablas movimiento_inventario_materia_prima y movimiento_inventario_producto_terminado. Se agregaron índices compuestos por fecha y almacén, se implementó paginación lazy loading y caché Redis de 10 minutos.',
        '2025-10-06 16:00:00',
        '2025-10-06 09:30:00', '2025-10-06 16:00:00', 390, 5
    );

    -- Ticket 10: Notificaciones SMTP
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-09 10:30:00', v_usuario_edwin,
        'Sistema no envía notificaciones de stock bajo de materias primas - Correos de alerta no llegan al área logística',
        'MEDIA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Configuración incorrecta del servidor SMTP en application-dev.properties. Puerto 587 bloqueado por firewall. Se cambió a puerto 465 con SSL, se actualizaron credenciales de Gmail y se probó con 5 alertas de cuero sintético.',
        '2025-10-09 16:50:00',
        '2025-10-09 10:45:00', '2025-10-09 16:50:00', 365, 4
    );

    -- Ticket 11: PDF Órdenes
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-11 08:00:00', v_usuario_fabiola,
        'Error al generar PDF de órdenes de compra de materia prima - Documento aparece corrupto o vacío',
        'MEDIA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Librería iText PDF versión 5.5.13 con bug conocido. Se actualizó a iText 7.2.5, se ajustó el código del OrdenCompraController para la nueva API y se agregó logo de Frederick EIRL al encabezado del PDF.',
        '2025-10-11 15:30:00',
        '2025-10-11 08:30:00', '2025-10-11 15:30:00', 420, 5
    );

    -- Ticket 12: Dashboard
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-13 11:00:00', v_usuario_juan,
        'Dashboard de inventarios muestra estadísticas desactualizadas - Gráficos no reflejan ventas de productos de cuero del día',
        'MEDIA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Job programado de actualización de estadísticas detenido. Se reinició el scheduler de Spring (@Scheduled) que ejecuta cada hora, se verificó que el cronjob de actualización funcione correctamente y se refrescó el caché del dashboard.',
        '2025-10-13 17:00:00',
        '2025-10-13 11:20:00', '2025-10-13 17:00:00', 340, 4
    );

    -- Ticket 13: Módulo Proveedores
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-15 09:30:00', v_usuario_edwin,
        'Módulo de proveedores no permite actualizar información de contacto - Botón "Guardar" no responde',
        'MEDIA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Bug en validación de campo "telefono" en el frontend. Regex permitía solo 9 dígitos pero algunos proveedores tienen código de país. Se ajustó la expresión regular a /^[+]?[0-9]{9,15}$/ y se sincronizó con el DTO del backend.',
        '2025-10-15 17:20:00',
        '2025-10-15 09:50:00', '2025-10-15 17:20:00', 450, 5
    );

    -- Ticket 14: Búsqueda Productos
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-17 10:00:00', v_usuario_fabiola,
        'Búsqueda de productos terminados por SKU no funciona - Sistema no encuentra billeteras y carteras registradas',
        'MEDIA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Error en el endpoint /api/productos-terminados/buscar. Query SQL usaba LIKE con % pero no escapaba caracteres especiales. Se corrigió a búsqueda con UPPER() y TRIM(), se agregó índice en columna sku.',
        '2025-10-17 16:40:00',
        '2025-10-17 10:20:00', '2025-10-17 16:40:00', 380, 4
    );

    -- Ticket 15: Excel Export
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-19 08:30:00', v_usuario_juan,
        'Exportación a Excel de inventario de materias primas genera archivo vacío - Formato .xlsx corrupto',
        'MEDIA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Problema con versión de Apache POI 4.1.2 incompatible con Java 17. Se actualizó a POI 5.2.3, se corrigió el código de generación del ExcelController y se probó exportación con 250 registros de cuero, hilos y herrajes.',
        '2025-10-19 15:40:00',
        '2025-10-19 08:50:00', '2025-10-19 15:40:00', 410, 5
    );

    -- ------------------------------------------------------------------------
    -- TICKETS PRIORIDAD BAJA (SLA: 1440 minutos)
    -- ------------------------------------------------------------------------

    -- Ticket 16: RPA Scheduler
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-08 09:00:00', v_usuario_edwin,
        'Sincronización RPA con Google Sheets más lenta de lo esperado - Actualiza cada 30 min en lugar de cada 15 min como configurado',
        'BAJA', 'RESUELTO', v_agente_frederick, 'INCIDENTE',
        'Se ajustó el trigger programado del flujo de Power Automate Desktop de 30 a 15 minutos. Se verificó que el script de Python helper se ejecute correctamente y se optimizó el tiempo de lectura/escritura en Google Sheets.',
        '2025-10-09 05:00:00',
        '2025-10-08 10:00:00', '2025-10-09 05:00:00', 1140, 4
    );

    -- Ticket 17: UI CSS (FUERA DE SLA)
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-10 14:00:00', v_usuario_fabiola,
        'Interfaz de módulo de clientes con problemas visuales - Botón "Registrar Cliente" desalineado en navegador Chrome',
        'BAJA', 'RESUELTO', v_agente_jhon, 'INCIDENTE',
        'Problema de CSS en componente ClienteForm.jsx. Flexbox no funcionaba correctamente en Chrome 129. Se agregó vendor prefix -webkit- y se probó en Chrome, Firefox, Edge y Safari.',
        '2025-10-11 15:20:00',
        '2025-10-10 15:00:00', '2025-10-11 15:20:00', 1460, 3
    );

    -- Ticket 18: Nuevo Reporte
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-12 10:00:00', v_usuario_juan,
        'Solicitud: Implementar nuevo reporte "Top 10 Productos de Cuero Más Vendidos del Mes" para análisis de ventas',
        'BAJA', 'RESUELTO', v_agente_frederick, 'PROBLEMA',
        'Se implementó el endpoint GET /api/reportes/productos-top-vendidos con query SQL optimizada que agrupa por producto_terminado_id. Se agregó caché de 1 hora, se creó vista en el frontend con gráfico de barras usando Chart.js.',
        '2025-10-13 08:30:00',
        '2025-10-12 11:00:00', '2025-10-13 08:30:00', 1290, 5
    );

    -- Ticket 19: Configuración IA (FUERA DE SLA)
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-14 08:00:00', v_usuario_edwin,
        'Consulta: ¿Cómo personalizar respuestas del Agente IA de WhatsApp para incluir catálogo de productos de cuero con precios?',
        'BAJA', 'RESUELTO', v_agente_jhon, 'PROBLEMA',
        'Se proporcionó documentación detallada sobre configuración de n8n. Se realizó sesión de capacitación de 45 minutos mostrando cómo editar el prompt de Gemini y agregar nodos de consulta al sistema de inventarios para obtener precios en tiempo real.',
        '2025-10-15 09:00:00',
        '2025-10-14 09:00:00', '2025-10-15 09:00:00', 1440, 2
    );

    -- Ticket 20: Filtro por Fechas
    INSERT INTO ticket_soporte (
        fecha_reporte, usuario_reporta_id, descripcion, prioridad, estado,
        responsable_asignado_id, tipo, solucion, fecha_cierre,
        fecha_inicio_atencion, fecha_resolucion, duracion_atencion_minutos, calificacion
    ) VALUES (
        '2025-10-16 13:00:00', v_usuario_fabiola,
        'Mejora sugerida: Agregar filtro por rango de fechas en historial de movimientos de inventario de materias primas',
        'BAJA', 'RESUELTO', v_agente_frederick, 'PROBLEMA',
        'Se implementó DateRangePicker en el componente MovimientosHistorial.jsx usando react-datepicker. Se agregó lógica de filtrado en el backend con @RequestParam fechaInicio y fechaFin. Funcionalidad desplegada y probada.',
        '2025-10-17 07:20:00',
        '2025-10-16 14:00:00', '2025-10-17 07:20:00', 1040, 5
    );

    RAISE NOTICE '20 tickets creados exitosamente';

END $$;

-- ================================================================================
-- PASO 3: VERIFICAR DATOS CREADOS
-- ================================================================================

-- Verificar usuarios
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

-- Verificar tickets con cumplimiento SLA
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
    LEFT(t.descripcion, 80) AS descripcion_corta
FROM ticket_soporte t
JOIN usuario u ON t.usuario_reporta_id = u.id
JOIN usuario r ON t.responsable_asignado_id = r.id
WHERE t.fecha_reporte BETWEEN '2025-10-01' AND '2025-10-20'
ORDER BY t.fecha_reporte;

-- ================================================================================
-- PASO 4: CÁLCULO DE INDICADORES
-- ================================================================================

-- OE4-I1: Porcentaje de cumplimiento SLA
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

-- OE4-I2: Índice de satisfacción (calificaciones 4 y 5)
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

-- Distribución por prioridad
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
-- RESULTADOS ESPERADOS:
-- - OE4-I1: 90% de cumplimiento SLA (18/20 tickets)
-- - OE4-I2: 86.67% de satisfacción (13/15 con calificación 4-5)
-- ================================================================================
-- NOTAS FINALES:
-- 1. Password usado: "password123" (hash BCrypt con fuerza 10)
-- 2. Telefono como BIGINT sin comillas
-- 3. Columnas snake_case según convención PostgreSQL
-- 4. Constraint UNIQUE en rol.rol necesaria
-- 5. Ajustar v_empresa_id según tu base de datos
-- ================================================================================
