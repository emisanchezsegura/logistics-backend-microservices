package ar.edu.utnfc.backend.atencion.service;

import ar.edu.utnfc.backend.atencion.model.Cliente;
import ar.edu.utnfc.backend.atencion.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public Cliente crear(Cliente c) {
        return repo.save(c);
    }

    public Cliente buscar(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ==========================================================
    // 🔹 Actualizar solo el teléfono del cliente
    // ==========================================================
    public Cliente actualizarTelefono(Long id, String nuevoTelefono) {

        Cliente c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Cliente no encontrado con id " + id));

        c.setTelefono(nuevoTelefono);

        return repo.save(c);
    }
}
