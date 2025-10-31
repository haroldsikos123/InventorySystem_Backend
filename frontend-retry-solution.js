// ========================================================================
// SOLUCIÓN FRONTEND: Manejo de Errores con Retry Automático
// Para resolver problemas de secuencias desincronizadas en PostgreSQL
// ========================================================================

/**
 * Servicio de Retry Automático para Frontend
 * Maneja automáticamente los errores 409/500 de constraint violations
 */
class RetryService {
    static MAX_RETRIES = 3;
    static RETRY_DELAY_BASE = 1000; // 1 segundo

    /**
     * Ejecuta una petición HTTP con reintentos automáticos
     * @param {Function} requestFn - Función que retorna una Promise con la petición HTTP
     * @param {string} operationName - Nombre de la operación para logs
     * @returns {Promise} - Resultado de la operación
     */
    static async executeWithRetry(requestFn, operationName = 'Operación') {
        let lastError = null;
        
        for (let attempt = 1; attempt <= this.MAX_RETRIES; attempt++) {
            try {
                const result = await requestFn();
                
                if (attempt > 1) {
                    console.log(`✅ ${operationName} exitosa en intento ${attempt}`);
                }
                
                return result;
                
            } catch (error) {
                lastError = error;
                const isRetriableError = this.isRetriableError(error);
                
                if (isRetriableError && attempt < this.MAX_RETRIES) {
                    console.warn(`🔄 Intento ${attempt} de ${operationName}: Error de secuencia detectado. Reintentando...`);
                    
                    // Delay progresivo: 1s, 2s, 3s
                    const delay = this.RETRY_DELAY_BASE * attempt;
                    await this.sleep(delay);
                    continue;
                } else {
                    // Error no recuperable o agotados los intentos
                    console.error(`❌ ${operationName} falló definitivamente:`, error);
                    throw error;
                }
            }
        }
        
        throw new Error(`${operationName} falló después de ${this.MAX_RETRIES} intentos: ${lastError.message}`);
    }
    
    /**
     * Determina si un error es recuperable (secuencia desincronizada)
     * @param {Error} error - Error de la petición HTTP
     * @returns {boolean} - true si es recuperable
     */
    static isRetriableError(error) {
        if (!error.response) return false;
        
        const status = error.response.status;
        const errorText = error.response.data?.error?.toLowerCase() || '';
        const message = error.message?.toLowerCase() || '';
        
        // Errores 409 (Conflict) o 500 (Internal Server Error)
        const isConflictOrServer = status === 409 || status === 500;
        
        // Buscar indicadores de problemas de secuencia/ID duplicado
        const isDuplicateKeyError = 
            errorText.includes('llave duplicada') ||
            errorText.includes('duplicate key') ||
            errorText.includes('unique constraint') ||
            errorText.includes('restricción de unicidad') ||
            message.includes('constraint violation');
            
        return isConflictOrServer && isDuplicateKeyError;
    }
    
    /**
     * Utility para pausar ejecución
     * @param {number} ms - Milisegundos a esperar
     */
    static sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
}

// ========================================================================
// EJEMPLOS DE USO EN DIFERENTES FRAMEWORKS
// ========================================================================

/**
 * EJEMPLO 1: Axios + Vanilla JavaScript
 */
async function registrarProveedor(proveedorData) {
    try {
        const response = await RetryService.executeWithRetry(
            () => axios.post('/api/proveedores/registrar', proveedorData),
            'Registro de Proveedor'
        );
        
        console.log('✅ Proveedor registrado:', response.data);
        return response.data;
        
    } catch (error) {
        console.error('❌ Error definitivo registrando proveedor:', error);
        
        // Mostrar mensaje amigable al usuario
        if (RetryService.isRetriableError(error)) {
            alert('Error temporal del sistema. Por favor intenta nuevamente en unos momentos.');
        } else {
            alert('Error registrando proveedor: ' + error.response?.data?.error || error.message);
        }
        
        throw error;
    }
}

/**
 * EJEMPLO 2: Angular HttpClient
 */
// Servicio Angular
/*
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError, delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProveedorService {
  constructor(private http: HttpClient) {}
  
  registrarProveedor(proveedor: any): Observable<any> {
    return this.http.post('/api/proveedores/registrar', proveedor)
      .pipe(
        retry({
          count: 3,
          delay: (error, retryCount) => {
            if (this.isRetriableError(error)) {
              console.warn(`🔄 Reintentando registro (${retryCount}/3)...`);
              return timer(1000 * retryCount); // Delay progresivo
            }
            return throwError(error);
          }
        }),
        catchError(error => {
          console.error('❌ Error registrando proveedor:', error);
          return throwError(error);
        })
      );
  }
  
  private isRetriableError(error: any): boolean {
    const status = error.status;
    const message = error.error?.error?.toLowerCase() || '';
    
    return (status === 409 || status === 500) && 
           (message.includes('llave duplicada') || 
            message.includes('duplicate key') || 
            message.includes('unique constraint'));
  }
}
*/

/**
 * EJEMPLO 3: React + Fetch API
 */
/*
import { useState } from 'react';

function useProveedorSubmit() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  const registrarProveedor = async (proveedorData) => {
    setLoading(true);
    setError(null);
    
    try {
      const result = await RetryService.executeWithRetry(
        () => fetch('/api/proveedores/registrar', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(proveedorData)
        }).then(response => {
          if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
          }
          return response.json();
        }),
        'Registro de Proveedor'
      );
      
      return result;
      
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };
  
  return { registrarProveedor, loading, error };
}
*/

/**
 * EJEMPLO 4: Vue.js + Axios
 */
/*
// Composable de Vue 3
import { ref } from 'vue';
import axios from 'axios';

export function useProveedorAPI() {
  const loading = ref(false);
  const error = ref(null);
  
  const registrarProveedor = async (proveedorData) => {
    loading.value = true;
    error.value = null;
    
    try {
      const response = await RetryService.executeWithRetry(
        () => axios.post('/api/proveedores/registrar', proveedorData),
        'Registro de Proveedor'
      );
      
      return response.data;
      
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };
  
  return {
    registrarProveedor,
    loading,
    error
  };
}
*/

// ========================================================================
// INTERCEPTOR GLOBAL (Recomendado)
// ========================================================================

/**
 * Interceptor Axios Global para manejar automáticamente todos los reintentos
 */
/*
import axios from 'axios';

// Crear instancia de Axios con interceptor de retry
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

// Interceptor de respuesta para manejar errores automáticamente
api.interceptors.response.use(
  response => response,
  async error => {
    const { config } = error;
    
    // Evitar loops infinitos
    if (!config || config.__retryCount >= 3) {
      return Promise.reject(error);
    }
    
    config.__retryCount = config.__retryCount || 0;
    
    // Solo reintentar en errores específicos de secuencia
    if (RetryService.isRetriableError(error)) {
      config.__retryCount += 1;
      
      console.warn(`🔄 Reintentando petición ${config.url} (${config.__retryCount}/3)...`);
      
      // Delay progresivo
      await RetryService.sleep(1000 * config.__retryCount);
      
      return api(config);
    }
    
    return Promise.reject(error);
  }
);

export default api;
*/

// ========================================================================
// EXPORTAR PARA USO
// ========================================================================
export default RetryService;