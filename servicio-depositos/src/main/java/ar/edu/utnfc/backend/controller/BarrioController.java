package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Barrio;
import ar.edu.utnfc.backend.service.BarrioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barrios")
public class BarrioController {

    private final BarrioService barrioService;

    public BarrioController(BarrioService barrioService) {
        this.barrioService = barrioService;
    }

    @GetMapping
    public ResponseEntity<List<Barrio>> obtenerTodos() {
        return ResponseEntity.ok(barrioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barrio> obtenerPorId(@PathVariable Long id) {
        return barrioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/localidad/{nombreLocalidad}")
    public ResponseEntity<List<Barrio>> obtenerPorLocalidad(@PathVariable String nombreLocalidad) {
        return ResponseEntity.ok(barrioService.obtenerPorLocalidad(nombreLocalidad));
    }

    @PostMapping
    public ResponseEntity<Barrio> crear(@RequestBody Barrio barrio) {
        return ResponseEntity.ok(barrioService.guardar(barrio));
    }
}
