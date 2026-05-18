package ar.edu.utnfc.backend.controller;


import ar.edu.utnfc.backend.model.Estado;
import ar.edu.utnfc.backend.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoRepository estadoRepository;

    @PostMapping
    public ResponseEntity<Estado> crear(@RequestBody Estado estado) {
        return ResponseEntity.ok(estadoRepository.save(estado));
    }

    @GetMapping
    public ResponseEntity<List<Estado>> listar() {
        return ResponseEntity.ok(estadoRepository.findAll());
    }
}
