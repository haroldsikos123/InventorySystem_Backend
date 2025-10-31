-- ================================================================================
-- SCRIPT SQL: Crear tabla ticket_actividad para el seguimiento de actividades
-- ================================================================================

-- Crear tabla para registrar actividades de tickets
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

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_ticket_actividad_ticket_id ON ticket_actividad(ticket_id);
CREATE INDEX IF NOT EXISTS idx_ticket_actividad_fecha ON ticket_actividad(fecha_actividad DESC);

-- Comentarios para documentar
COMMENT ON TABLE ticket_actividad IS 'Registro de actividades realizadas en los tickets de soporte';
COMMENT ON COLUMN ticket_actividad.ticket_id IS 'ID del ticket relacionado';
COMMENT ON COLUMN ticket_actividad.usuario_id IS 'ID del usuario que realizó la actividad (puede ser NULL para actividades del sistema)';
COMMENT ON COLUMN ticket_actividad.descripcion IS 'Descripción de la actividad realizada';
COMMENT ON COLUMN ticket_actividad.fecha_actividad IS 'Fecha y hora cuando se realizó la actividad';

-- Verificar estructura de la tabla
SELECT 
    column_name, 
    data_type, 
    is_nullable, 
    column_default 
FROM information_schema.columns 
WHERE table_name = 'ticket_actividad' 
ORDER BY ordinal_position;