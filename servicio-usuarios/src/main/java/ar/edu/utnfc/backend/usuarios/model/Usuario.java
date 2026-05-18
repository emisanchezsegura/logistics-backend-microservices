package ar.edu.utnfc.backend.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa a un usuario interno del sistema Logística.
 * Cada usuario se autentica a través de Keycloak, pero se mantiene
 * una copia local sincronizada con sus roles y datos básicos.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario utilizado para autenticación en Keycloak.
     * Es único dentro del sistema.
     */
    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    /**
     * Contraseña local (en escenarios reales se guarda cifrada).
     * En este proyecto puede quedar almacenada para sincronización.
     */
    @Column(nullable = false)
    private String contrasena;

    /**
     * Rol asignado al usuario, coincide con los roles definidos en Keycloak.
     * Los roles determinan los endpoints a los que puede acceder vía Gateway.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /**
     * Identificador lógico de la persona asociada al usuario:
     * - Cliente (si es un usuario del tipo CLIENTE)
     * - Empleado (si es OPERADOR o TRANSPORTISTA)
     */
    @Column(nullable = false)
    private Long idPersona;
}
