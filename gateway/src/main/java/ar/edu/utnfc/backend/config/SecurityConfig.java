package ar.edu.utnfc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            KeycloakRealmRoleConverter roleConverter
    ) {

        // 🔥 ADAPTADOR NECESARIO PARA WEBFLUX
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(roleConverter);

        var reactiveConverter = new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);

        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges

                // ============================================================
                // 🔓 Swagger público
                // ============================================================
                .pathMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                    .permitAll()

                // ============================================================
                // 🔐 DEPÓSITOS (solo OPERADOR)
                // ============================================================
                .pathMatchers(HttpMethod.GET, "/depositos/api/depositos/**")
                    .hasRole("OPERADOR")
                .pathMatchers(HttpMethod.POST, "/depositos/api/depositos/**")
                    .hasRole("OPERADOR")
                .pathMatchers(HttpMethod.PUT, "/depositos/api/depositos/**")
                    .hasRole("OPERADOR")
                .pathMatchers(HttpMethod.DELETE, "/depositos/api/depositos/**")
                    .hasRole("OPERADOR")

                .pathMatchers("/depositos/api/estadias/**")
                    .hasRole("OPERADOR")

                // ============================================================
                // 🔐 CAMIONES
                // ============================================================
                .pathMatchers(HttpMethod.GET, "/transporte/api/camiones/**")
                    .hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.POST, "/transporte/api/camiones/**")
                    .hasRole("OPERADOR")
                .pathMatchers(HttpMethod.DELETE, "/transporte/api/camiones/**")
                    .hasRole("ADMIN")

                // ============================================================
                // 🔐 TRANSPORTISTAS
                // ============================================================
                .pathMatchers(HttpMethod.GET, "/transporte/api/transportistas/**")
                    .hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.POST, "/transporte/api/transportistas/**")
                    .hasRole("OPERADOR")
                .pathMatchers(HttpMethod.DELETE, "/transporte/api/transportistas/**")
                    .hasRole("ADMIN")

                // ============================================================
                // 🔐 TRAMOS (solo transportista)
                // ============================================================
                .pathMatchers(HttpMethod.PUT, "/rutas/api/tramos/*/iniciar")
                    .hasRole("TRANSPORTISTA")
                .pathMatchers(HttpMethod.PUT, "/rutas/api/tramos/*/finalizar")
                    .hasRole("TRANSPORTISTA")


                // ============================================================
                // 🔐 ATENCIÓN
                // ============================================================

                // Registrar nueva solicitud → CLIENTE u OPERADOR
                .pathMatchers(HttpMethod.POST, "/atencion/api/solicitudes")
                    .hasAnyRole("CLIENTE", "OPERADOR")

                // Consultar estado de transporte de un contenedor → solo CLIENTE
                .pathMatchers(HttpMethod.GET, "/atencion/api/contenedores/pendientes")
                    .hasRole("CLIENTE")

                .pathMatchers(HttpMethod.PUT, "/atencion/api/solicitudes/*/asignar-camion")
                .hasAnyRole("OPERADOR", "ADMIN")


                // ============================================================
                // 🔒 Todo lo demás autenticado
                // ============================================================
                .anyExchange().authenticated()
            )
            // 🔥 APLICAR TU CONVERTER DE ROLES
            .oauth2ResourceServer(oauth -> oauth
                .jwt(jwt -> jwt.jwtAuthenticationConverter(reactiveConverter))
            );

        return http.build();
    }
}
