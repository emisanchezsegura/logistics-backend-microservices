package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Localidad;
import ar.edu.utnfc.backend.service.LocalidadService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController {

    private final LocalidadService localidadService;

    public LocalidadController(LocalidadService localidadService) {
        this.localidadService = localidadService;
    }

    @GetMapping
    public ResponseEntity<List<Localidad>> obtenerTodas() {
        return ResponseEntity.ok(localidadService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Localidad> obtenerPorId(@PathVariable Long id) {
        return localidadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Localidad> crear(@RequestBody Localidad localidad) {
        return ResponseEntity.ok(localidadService.guardar(localidad));
    }
}
