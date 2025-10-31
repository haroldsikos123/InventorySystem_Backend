package com.inventorysystem_project.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * Servicio para manejar operaciones con reintentos automáticos
 * Soluciona el problema de secuencias desincronizadas en PostgreSQL
 */
@Service
public class RetryService {
    
    private static final Logger logger = LoggerFactory.getLogger(RetryService.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 100;
    
    /**
     * Ejecuta una operación con reintentos automáticos para manejar errores de ID duplicado
     * @param operation Operación a ejecutar
     * @param entityName Nombre de la entidad para logs
     * @param <T> Tipo de retorno
     * @return Resultado de la operación
     * @throws RuntimeException si falla después de todos los reintentos
     */
    public <T> T executeWithRetry(Supplier<T> operation, String entityName) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                T result = operation.get();
                
                if (attempt > 1) {
                    logger.info("✅ {} registrado exitosamente en intento {}", entityName, attempt);
                }
                
                return result;
                
            } catch (DataIntegrityViolationException e) {
                lastException = e;
                String errorMsg = e.getMessage();
                
                // Verificar si es error de llave duplicada (secuencia desincronizada)
                if (errorMsg != null && (errorMsg.contains("duplicate key") || 
                                       errorMsg.contains("llave duplicada") || 
                                       errorMsg.contains("violates unique constraint"))) {
                    
                    logger.warn("🔄 Intento {} para {}: Secuencia desincronizada detectada. Reintentando...", 
                               attempt, entityName);
                    
                    if (attempt < MAX_RETRIES) {
                        try {
                            Thread.sleep(RETRY_DELAY_MS * attempt); // Incrementar delay
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException("Operación interrumpida", ie);
                        }
                        continue; // Reintentar
                    }
                } else {
                    // Error diferente, no reintentar
                    logger.error("❌ Error de integridad no relacionado con secuencias en {}: {}", 
                                entityName, errorMsg);
                    throw e;
                }
            } catch (Exception e) {
                // Otros tipos de error, no reintentar
                logger.error("❌ Error no recuperable en {}: {}", entityName, e.getMessage());
                throw new RuntimeException("Error no recuperable al registrar " + entityName, e);
            }
        }
        
        // Si llegamos aquí, agotamos todos los intentos
        String errorMessage = lastException != null ? lastException.getMessage() : "Error desconocido";
        logger.error("❌ Agotados todos los reintentos para {}. Último error: {}", 
                    entityName, errorMessage);
        
        throw new RuntimeException(
            String.format("No se pudo registrar %s después de %d intentos. " +
                         "Posible problema de secuencias desincronizadas en base de datos.", 
                         entityName, MAX_RETRIES), 
            lastException
        );
    }
}