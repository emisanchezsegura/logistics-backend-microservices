package ar.edu.utnfc.backend.usuarios.model;

/**
 * Roles principales del sistema que interactúan con el Gateway.
 * Deben existir también en Keycloak bajo el mismo nombre.
 */
public enum Rol {

    /**
     * CLIENTE → puede registrar solicitudes y consultar el estado
     * de sus contenedores.
     */
    CLIENTE,

    /**
     * OPERADOR → empleado del sistema, gestiona clientes, depósitos,
     * camiones, tarifas y rutas. También representa el rol ADMIN
     * en Keycloak si se desea unificar.
     */
    OPERADOR,

    /**
     * TRANSPORTISTA → chofer o camionero, registra inicio y fin de tramos
     * asignados en el sistema.
     */
    TRANSPORTISTA
}
