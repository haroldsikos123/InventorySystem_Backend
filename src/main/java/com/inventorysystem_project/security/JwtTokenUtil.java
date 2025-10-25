package com.inventorysystem_project.security;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    // 7 horas de validez del token en milisegundos
    public static final long JWT_TOKEN_VALIDITY = 7 * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    // Convierte la clave secreta a tipo Key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Obtiene el username (subject) del token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Obtiene la fecha de expiración del token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Obtiene cualquier claim del token usando un Function
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Obtiene todos los claims del token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica si el token expiró (considera expiración nula como expirado)
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if (expiration == null) {
            return true; // si no tiene expiración, consideramos expirado
        }
        return expiration.before(new Date());
    }

    // Genera un token JWT para un usuario dado
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nombre", "rosa"); // ejemplo de claim personalizado
        claims.put("role", userDetails.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .collect(Collectors.joining()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Construye el token con claims, sujeto, fecha emisión y expiración
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Expiración activa
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Valida el token comparando username y expiración
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
