-- =====================================================
-- Script para arreglar secuencias de ID en PostgreSQL
-- Ejecutar este script para sincronizar las secuencias con los datos existentes
-- =====================================================

-- Verificar el estado ANTES del arreglo
SELECT 'ESTADO ANTES DEL ARREGLO:' as status;
SELECT 
    sequencename,
    last_value,
    is_called,
    (SELECT COALESCE(MAX(id), 0) FROM usuario) as max_id_tabla
FROM pg_sequences 
WHERE sequencename LIKE 'usuario_id_seq';

-- ARREGLAR SECUENCIAS
-- ===================

-- Usuario
SELECT 'Arreglando usuario_id_seq...' as info;
SELECT setval('usuario_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM usuario));

-- Empresa
SELECT 'Arreglando empresa_id_seq...' as info;
SELECT setval('empresa_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM empresa));

-- Proveedor
SELECT 'Arreglando proveedor_id_seq...' as info;
SELECT setval('proveedor_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM proveedor));

-- Producto Terminado
SELECT 'Arreglando producto_terminado_id_seq...' as info;
SELECT setval('producto_terminado_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM producto_terminado));

-- Almacen
SELECT 'Arreglando almacen_id_seq...' as info;
SELECT setval('almacen_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM almacen));

-- Materia Prima
SELECT 'Arreglando materia_prima_id_seq...' as info;
SELECT setval('materia_prima_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM materia_prima));

-- Rol
SELECT 'Arreglando rol_id_seq...' as info;
SELECT setval('rol_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rol));

-- Cliente
SELECT 'Arreglando cliente_id_seq...' as info;
SELECT setval('cliente_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM cliente));

-- Orden Compra
SELECT 'Arreglando orden_compra_id_seq...' as info;
SELECT setval('orden_compra_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM orden_compra));

-- Detalle Orden Compra
SELECT 'Arreglando detalle_orden_compra_id_seq...' as info;
SELECT setval('detalle_orden_compra_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM detalle_orden_compra));

-- Movimiento Inventario Materia Prima
SELECT 'Arreglando movimiento_inventario_materia_prima_id_seq...' as info;
SELECT setval('movimiento_inventario_materia_prima_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM movimiento_inventario_materia_prima));

-- Movimiento Inventario Producto Terminado
SELECT 'Arreglando movimiento_inventario_producto_terminado_id_seq...' as info;
SELECT setval('movimiento_inventario_producto_terminado_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM movimiento_inventario_producto_terminado));

-- Stock Almacen Materia Prima
SELECT 'Arreglando stock_almacen_materia_prima_id_seq...' as info;
SELECT setval('stock_almacen_materia_prima_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM stock_almacen_materia_prima));

-- Stock Almacen Producto Terminado
SELECT 'Arreglando stock_almacen_producto_terminado_id_seq...' as info;
SELECT setval('stock_almacen_producto_terminado_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM stock_almacen_producto_terminado));

-- VERIFICAR ESTADO FINAL
-- ======================
SELECT '============ ESTADO FINAL DE SECUENCIAS ============' as status;
SELECT 
    schemaname,
    sequencename,
    last_value,
    start_value,
    increment_by,
    is_called,
    CASE 
        WHEN sequencename = 'usuario_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM usuario)
        WHEN sequencename = 'empresa_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM empresa)
        WHEN sequencename = 'proveedor_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM proveedor)
        WHEN sequencename = 'producto_terminado_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM producto_terminado)
        WHEN sequencename = 'almacen_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM almacen)
        WHEN sequencename = 'materia_prima_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM materia_prima)
        WHEN sequencename = 'rol_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM rol)
        WHEN sequencename = 'cliente_id_seq' THEN (SELECT COALESCE(MAX(id), 0) FROM cliente)
        ELSE NULL
    END as max_id_en_tabla
FROM pg_sequences 
WHERE schemaname = 'public'
AND sequencename LIKE '%_id_seq'
ORDER BY sequencename;

SELECT '✅ ¡Secuencias arregladas! Ahora puedes crear nuevos registros sin problemas.' as mensaje;