package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Camion;
import ar.edu.utnfc.backend.service.CamionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/camiones")
@CrossOrigin("*")
public class CamionController {

    private final CamionService service;

    public CamionController(CamionService service) {
        this.service = service;
    }

    // 🔹 Endpoint existente: listar todos
    @GetMapping
    public List<Camion> listar() {
        return service.listar();
    }

    // 🔹 Endpoint existente: guardar
    @PostMapping
    public Camion guardar(@RequestBody Camion camion) {
        return service.guardar(camion);
    }

    // 🔹 Endpoint existente: eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // ✅ NUEVO ENDPOINT: filtra camiones disponibles por peso y volumen
    @GetMapping("/disponibles")
    public List<Camion> obtenerDisponibles(
            @RequestParam Double peso,
            @RequestParam Double volumen
    ) {
        return service.obtenerDisponibles(peso, volumen);
    }

    // 🔹 Endpoint adicional del archivo 2 (preservado)
    @GetMapping("/{id}")
    public ResponseEntity<Camion> obtenerCamionPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
