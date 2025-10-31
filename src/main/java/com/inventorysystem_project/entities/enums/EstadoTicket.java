package com.inventorysystem_project.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoTicket {
    ABIERTO,
    EN_PROGRESO,
    RESUELTO,
    CERRADO;

    @JsonCreator
    public static EstadoTicket fromString(String value) {
        if (value == null) {
            return null;
        }
        
        // Normalizar el valor para aceptar variaciones
        String normalizedValue = value.toUpperCase().trim();
        
        // Manejar alias comunes
        switch (normalizedValue) {
            case "EN_PROCESO":
                return EN_PROGRESO;
            case "EN_PROGRESO":
                return EN_PROGRESO;
            case "ABIERTO":
                return ABIERTO;
            case "RESUELTO":
                return RESUELTO;
            case "CERRADO":
                return CERRADO;
            default:
                throw new IllegalArgumentException("Estado de ticket no válido: " + value);
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}