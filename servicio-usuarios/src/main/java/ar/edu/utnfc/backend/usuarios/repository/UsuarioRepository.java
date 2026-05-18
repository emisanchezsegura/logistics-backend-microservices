package ar.edu.utnfc.backend.usuarios.repository;

import ar.edu.utnfc.backend.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad Usuario.
 * Provee operaciones CRUD y búsquedas personalizadas.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario (único).
     * @param nombreUsuario nombre del usuario
     * @return un Optional con el usuario si existe
     */
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    /**
     * Busca un usuario asociado a una persona (Cliente u Operador).
     * @param idPersona identificador lógico de la persona asociada
     * @return un Optional con el usuario si existe
     */
    Optional<Usuario> findByIdPersona(Long idPersona);
}
