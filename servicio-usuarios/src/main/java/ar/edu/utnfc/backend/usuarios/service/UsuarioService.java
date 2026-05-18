package ar.edu.utnfc.backend.usuarios.service;

import ar.edu.utnfc.backend.usuarios.model.Rol;
import ar.edu.utnfc.backend.usuarios.model.Usuario;
import ar.edu.utnfc.backend.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la lógica de negocio asociada a los usuarios.
 * Interactúa con la base de datos a través del UsuarioRepository.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Devuelve todos los usuarios registrados en el sistema.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     * Valida que no exista otro con el mismo nombre de usuario.
     */
    public Usuario crearUsuario(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (existente.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe: " + usuario.getNombreUsuario());
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su ID.
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Actualiza el rol de un usuario existente.
     */
    public Usuario actualizarRol(Long id, Rol nuevoRol) {
        Usuario usuario = buscarPorId(id);
        usuario.setRol(nuevoRol);
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario del sistema.
     */
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
