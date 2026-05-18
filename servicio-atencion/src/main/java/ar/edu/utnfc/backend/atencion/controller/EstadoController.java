package ar.edu.utnfc.backend.atencion.controller;

import ar.edu.utnfc.backend.atencion.model.Estado;
import ar.edu.utnfc.backend.atencion.repository.EstadoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estados")
@CrossOrigin("*")
public class EstadoController {

    private final EstadoRepository repo;

    public EstadoController(EstadoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Estado> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Estado crear(@RequestBody Estado estado) {
        return repo.save(estado);
    }
}
