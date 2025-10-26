package com.inventorysystem_project.security;
import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import com.inventorysystem_project.serviceimplements.JwtUserDetailsService;


//Clase 6
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader("Authorization");
    String username = null;
    String jwtToken = null;

    // 🔹 Evitar que el filtro se aplique a endpoints públicos
    String path = request.getRequestURI();
    if (path.startsWith("/authenticate") || path.startsWith("/usuarios/registrar") || path.startsWith("/roles/listar")) {
        chain.doFilter(request, response);
        return;
    }

    // 🔹 Si no hay token o no empieza con Bearer, continuar sin autenticar
    if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
        logger.warn("JWT Token no inicia con la palabra Bearer o no está presente");
        chain.doFilter(request, response);
        return;
    }

    // 🔹 Extraer y validar token
    try {
        jwtToken = requestTokenHeader.substring(7);
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
    } catch (IllegalArgumentException e) {
        logger.error("No se puede obtener el token JWT", e);
    } catch (ExpiredJwtException e) {
        logger.warn("El token JWT ha expirado", e);
    } catch (Exception e) {
        logger.error("Error procesando JWT", e);
    }

    // 🔹 Autenticar si todo es válido
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    chain.doFilter(request, response);
}

}
