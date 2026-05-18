package ar.edu.utnfc.backend.usuarios.controller;

import ar.edu.utnfc.backend.usuarios.model.Rol;
import ar.edu.utnfc.backend.usuarios.model.Usuario;
import ar.edu.utnfc.backend.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints del microservicio de usuarios.
 * Gestiona las operaciones CRUD y permite administrar roles.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * GET /usuarios
     * Lista todos los usuarios del sistema.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * GET /usuarios/{id}
     * Busca un usuario por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    /**
     * POST /usuarios
     * Crea un nuevo usuario.
     * Ejemplo de JSON:
     * {
     *   "nombreUsuario": "juan",
     *   "contrasena": "1234",
     *   "rol": "OPERADOR",
     *   "idPersona": 1
     * }
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevo);
    }

    /**
     * PUT /usuarios/{id}/rol
     * Actualiza el rol de un usuario.
     * Ejemplo de JSON:
     * {
     *   "nuevoRol": "TRANSPORTISTA"
     * }
     */
    @PutMapping("/{id}/rol")
    public ResponseEntity<Usuario> actualizarRol(
            @PathVariable Long id,
            @RequestBody Rol nuevoRol) {
        return ResponseEntity.ok(usuarioService.actualizarRol(id, nuevoRol));
    }

    /**
     * DELETE /usuarios/{id}
     * Elimina un usuario por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
