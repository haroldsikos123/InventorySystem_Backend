package com.inventorysystem_project.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    

    @Bean
    public static PasswordEncoder passwordEncoder() {
        // Mantenemos el factor de trabajo en 8 para un login más rápido
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // <-- CAMBIO: Se eliminó el método obsoleto 'configureGlobal'.
    // Spring Security moderno detecta automáticamente el UserDetailsService y PasswordEncoder.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Se aplica la configuración de CORS directamente a la cadena de filtros de seguridad
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/authenticate/**",
                        "/roles/**",
                        "/usuarios/registrar",
                        "/usuarios/listar"
                    ).permitAll()

                    // REGLA 1: Permitir a cualquier autenticado crear tickets (POST a /api/soporte/tickets)
                    .requestMatchers(HttpMethod.POST, "/api/soporte/tickets").authenticated()
                    // REGLA 2: GUEST (y otros) pueden leer recursos con GET
                    .requestMatchers(HttpMethod.GET, "/api/**").authenticated()

                    // REGLA 3: Comentarios y POSTs dentro de /api/soporte/tickets/** requieren al menos USER
                    .requestMatchers(HttpMethod.POST, "/api/soporte/tickets/**").hasAnyAuthority("USER", "ADMIN", "SOPORTE_N1", "SOPORTE_N2")

                    // REGLA 4: USER y superiores tienen permiso CUD en el resto de la API
                    .requestMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("USER", "ADMIN", "SOPORTE_N1", "SOPORTE_N2", "GESTOR_CAMBIOS", "CAB_MEMBER", "PROJECT_MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority("USER", "ADMIN", "SOPORTE_N1", "SOPORTE_N2", "GESTOR_CAMBIOS", "CAB_MEMBER", "PROJECT_MANAGER")
                    .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority("USER", "ADMIN", "SOPORTE_N1", "SOPORTE_N2", "GESTOR_CAMBIOS", "CAB_MEMBER", "PROJECT_MANAGER")

                    .anyRequest().authenticated()
                )

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Orígenes permitidos (tu frontend en Netlify y el entorno local para desarrollo)
        configuration.setAllowedOrigins(Arrays.asList("https://inventory-system-automation.netlify.app", "http://localhost:3000", "http://localhost:4200"));
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // Cabeceras permitidas
        configuration.setAllowedHeaders(List.of("*"));
        // Permitir credenciales (cookies, etc.)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar esta configuración a todas las rutas de la API
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
