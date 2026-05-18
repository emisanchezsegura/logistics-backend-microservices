package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Transportista;
import ar.edu.utnfc.backend.service.TransportistaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transportistas")
@CrossOrigin("*")
public class TransportistaController {
    private final TransportistaService service;
    public TransportistaController(TransportistaService service) { this.service = service; }

    @GetMapping
    public List<Transportista> listar() { return service.listar(); }

    @PostMapping
    public Transportista guardar(@RequestBody Transportista t) { return service.guardar(t); }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}