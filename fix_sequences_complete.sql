-- ========================================================================
-- SCRIPT DEFINITIVO PARA ARREGLAR SECUENCIAS DESINCRONIZADAS
-- Ejecutar cuando hayas subido registros manualmente o por migración
-- ========================================================================

-- Mostrar información inicial
SELECT '🔍 DIAGNÓSTICO DE SECUENCIAS DESINCRONIZADAS' as info;
SELECT '=================================================================' as separador;

-- Verificar estado de todas las secuencias vs datos
DO $$ 
DECLARE 
    tabla_nombre text;
    seq_nombre text;
    max_id bigint;
    seq_value bigint;
    diferencia bigint;
BEGIN
    RAISE NOTICE '📊 ESTADO ACTUAL DE SECUENCIAS:';
    RAISE NOTICE '';
    
    -- Array de tablas principales con sus secuencias
    FOR tabla_nombre, seq_nombre IN VALUES 
        ('usuario', 'usuario_id_seq'),
        ('empresa', 'empresa_id_seq'), 
        ('proveedor', 'proveedor_id_seq'),
        ('materia_prima', 'materia_prima_id_seq'),
        ('producto_terminado', 'producto_terminado_id_seq'),
        ('almacen', 'almacen_id_seq'),
        ('rol', 'rol_id_seq'),
        ('cliente', 'cliente_id_seq'),
        ('orden_compra', 'orden_compra_id_seq'),
        ('detalle_orden_compra', 'detalle_orden_compra_id_seq'),
        ('movimiento_inventario_materia_prima', 'movimiento_inventario_materia_prima_id_seq'),
        ('movimiento_inventario_producto_terminado', 'movimiento_inventario_producto_terminado_id_seq')
    LOOP
        -- Obtener máximo ID actual de la tabla
        EXECUTE format('SELECT COALESCE(MAX(id), 0) FROM %I', tabla_nombre) INTO max_id;
        
        -- Obtener valor actual de la secuencia
        EXECUTE format('SELECT last_value FROM %I', seq_nombre) INTO seq_value;
        
        diferencia := max_id - seq_value;
        
        IF diferencia > 0 THEN
            RAISE NOTICE '❌ %: MAX_ID=%, SEQ=%, DIFERENCIA=% (NECESITA ARREGLO)', 
                         upper(tabla_nombre), max_id, seq_value, diferencia;
        ELSE
            RAISE NOTICE '✅ %: MAX_ID=%, SEQ=%, OK', 
                         upper(tabla_nombre), max_id, seq_value;
        END IF;
    END LOOP;
    
    RAISE NOTICE '';
    RAISE NOTICE '🔧 INICIANDO REPARACIÓN AUTOMÁTICA...';
    RAISE NOTICE '';
END $$;

-- ========================================================================
-- REPARAR TODAS LAS SECUENCIAS AUTOMÁTICAMENTE
-- ========================================================================

-- Usuario
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM usuario INTO new_seq_val;
    PERFORM setval('usuario_id_seq', new_seq_val);
    RAISE NOTICE '✅ usuario_id_seq -> %', new_seq_val;
END $$;

-- Empresa  
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM empresa INTO new_seq_val;
    PERFORM setval('empresa_id_seq', new_seq_val);
    RAISE NOTICE '✅ empresa_id_seq -> %', new_seq_val;
END $$;

-- Proveedor
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM proveedor INTO new_seq_val;
    PERFORM setval('proveedor_id_seq', new_seq_val);
    RAISE NOTICE '✅ proveedor_id_seq -> %', new_seq_val;
END $$;

-- Materia Prima
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM materia_prima INTO new_seq_val;
    PERFORM setval('materia_prima_id_seq', new_seq_val);
    RAISE NOTICE '✅ materia_prima_id_seq -> %', new_seq_val;
END $$;

-- Producto Terminado
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM producto_terminado INTO new_seq_val;
    PERFORM setval('producto_terminado_id_seq', new_seq_val);
    RAISE NOTICE '✅ producto_terminado_id_seq -> %', new_seq_val;
END $$;

-- Almacen
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM almacen INTO new_seq_val;
    PERFORM setval('almacen_id_seq', new_seq_val);
    RAISE NOTICE '✅ almacen_id_seq -> %', new_seq_val;
END $$;

-- Rol
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM rol INTO new_seq_val;
    PERFORM setval('rol_id_seq', new_seq_val);
    RAISE NOTICE '✅ rol_id_seq -> %', new_seq_val;
END $$;

-- Cliente
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM cliente INTO new_seq_val;
    PERFORM setval('cliente_id_seq', new_seq_val);
    RAISE NOTICE '✅ cliente_id_seq -> %', new_seq_val;
END $$;

-- Orden Compra
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM orden_compra INTO new_seq_val;
    PERFORM setval('orden_compra_id_seq', new_seq_val);
    RAISE NOTICE '✅ orden_compra_id_seq -> %', new_seq_val;
END $$;

-- Detalle Orden Compra
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM detalle_orden_compra INTO new_seq_val;
    PERFORM setval('detalle_orden_compra_id_seq', new_seq_val);
    RAISE NOTICE '✅ detalle_orden_compra_id_seq -> %', new_seq_val;
END $$;

-- Movimiento Inventario Materia Prima
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM movimiento_inventario_materia_prima INTO new_seq_val;
    PERFORM setval('movimiento_inventario_materia_prima_id_seq', new_seq_val);
    RAISE NOTICE '✅ movimiento_inventario_materia_prima_id_seq -> %', new_seq_val;
END $$;

-- Movimiento Inventario Producto Terminado
DO $$ 
DECLARE new_seq_val bigint;
BEGIN
    SELECT COALESCE(MAX(id), 0) + 1 FROM movimiento_inventario_producto_terminado INTO new_seq_val;
    PERFORM setval('movimiento_inventario_producto_terminado_id_seq', new_seq_val);
    RAISE NOTICE '✅ movimiento_inventario_producto_terminado_id_seq -> %', new_seq_val;
END $$;

-- ========================================================================
-- VERIFICACIÓN FINAL
-- ========================================================================
SELECT '';
SELECT '🎯 VERIFICACIÓN FINAL:' as resultado;
SELECT '=================================' as separador;

DO $$ 
DECLARE 
    tabla_nombre text;
    seq_nombre text;
    max_id bigint;
    seq_value bigint;
    todo_ok boolean := true;
BEGIN    
    FOR tabla_nombre, seq_nombre IN VALUES 
        ('usuario', 'usuario_id_seq'),
        ('empresa', 'empresa_id_seq'), 
        ('proveedor', 'proveedor_id_seq'),
        ('materia_prima', 'materia_prima_id_seq'),
        ('producto_terminado', 'producto_terminado_id_seq'),
        ('almacen', 'almacen_id_seq'),
        ('rol', 'rol_id_seq'),
        ('cliente', 'cliente_id_seq')
    LOOP
        EXECUTE format('SELECT COALESCE(MAX(id), 0) FROM %I', tabla_nombre) INTO max_id;
        EXECUTE format('SELECT last_value FROM %I', seq_nombre) INTO seq_value;
        
        IF seq_value <= max_id THEN
            RAISE NOTICE '❌ %: Aún necesita corrección (MAX_ID=%, SEQ=%)', tabla_nombre, max_id, seq_value;
            todo_ok := false;
        ELSE
            RAISE NOTICE '✅ %: Correcto (MAX_ID=%, SEQ=%)', tabla_nombre, max_id, seq_value;
        END IF;
    END LOOP;
    
    IF todo_ok THEN
        RAISE NOTICE '';
        RAISE NOTICE '🎉 ¡TODAS LAS SECUENCIAS ESTÁN SINCRONIZADAS!';
        RAISE NOTICE '   Los próximos registros se insertarán correctamente.';
    ELSE
        RAISE NOTICE '';
        RAISE NOTICE '⚠️  Algunas secuencias necesitan revisión manual.';
    END IF;
END $$;