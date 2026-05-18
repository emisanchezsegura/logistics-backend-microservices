
package ar.edu.utnfc.backend.service;
import ar.edu.utnfc.backend.model.Transportista;
import ar.edu.utnfc.backend.repository.TransportistaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransportistaService {
    private final TransportistaRepository repo;
    public TransportistaService(TransportistaRepository repo) { this.repo = repo; }
    public List<Transportista> listar() { return repo.findAll(); }
    public Transportista guardar(Transportista t) { return repo.save(t); }
    public void eliminar(Long id) { repo.deleteById(id); }
}
