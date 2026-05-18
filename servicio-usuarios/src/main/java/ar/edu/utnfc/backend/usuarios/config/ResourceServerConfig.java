package ar.edu.utnfc.backend.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Configuración de seguridad para el microservicio de usuarios.
 * Valida tokens JWT emitidos por Keycloak (8091) y restringe el acceso
 * según los roles definidos (CLIENTE, OPERADOR, TRANSPORTISTA).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // No se usa sesión; el backend debe ser stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Desactiva CSRF (no necesario para APIs REST)
            .csrf(csrf -> csrf.disable())
            // Desactiva login form y basic auth
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Configura las reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // 👇 Endpoints públicos o de monitoreo (health check y actuator)
                .requestMatchers("/actuator/**", "/actuator/health", "/error", "/publico/**").permitAll()

                // 👇 Solo usuarios con rol OPERADOR pueden acceder a /usuarios/**
                .requestMatchers("/usuarios/**").hasRole("OPERADOR")

                // 👇 Cualquier otra petición requiere autenticación JWT
                .anyRequest().authenticated()
            )

            // Configura la aplicación como Resource Server (JWT)
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * Convierte los roles del token JWT (claim "realm_access.roles")
     * al formato que espera Spring Security: "ROLE_<ROL>"
     * Ejemplo: CLIENTE -> ROLE_CLIENTE
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return List.of();
            }

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        });
        return converter;
    }
}
