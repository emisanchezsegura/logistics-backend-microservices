package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Tramo;
import ar.edu.utnfc.backend.service.TramoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    @GetMapping
    public ResponseEntity<List<Tramo>> listar() {
        return ResponseEntity.ok(tramoService.listarTramos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tramo> obtenerPorId(@PathVariable Long id) {
        return tramoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tramo> crear(@RequestBody Tramo tramo) {
        return ResponseEntity.ok(tramoService.guardar(tramo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tramoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tramo> actualizar(
            @PathVariable Long id,
            @RequestBody Tramo tramoActualizado
    ) {
        return ResponseEntity.ok(tramoService.actualizar(id, tramoActualizado));
    }

    // ========================================================
    // RF7 - INICIAR TRAMO
    // ========================================================
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<?> iniciarTramo(@PathVariable Long id) {

        tramoService.iniciarTramo(id);

        return ResponseEntity.ok("Tramo iniciado");
    }

    // ========================================================
    // RF7 - FINALIZAR TRAMO
    // ========================================================
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarTramo(@PathVariable Long id) {

        tramoService.finalizarTramo(id);

        return ResponseEntity.ok("Tramo finalizado");
    }
}
