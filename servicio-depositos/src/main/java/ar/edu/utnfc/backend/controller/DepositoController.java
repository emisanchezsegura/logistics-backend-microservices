package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Deposito;
import ar.edu.utnfc.backend.service.DepositoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    // ✅ GET: listar todos los depósitos
    @GetMapping
    public ResponseEntity<List<Deposito>> obtenerTodos() {
        return ResponseEntity.ok(depositoService.obtenerTodos());
    }

    // ✅ GET: obtener un depósito por ID
    @GetMapping("/{id}")
    public ResponseEntity<Deposito> obtenerPorId(@PathVariable Long id) {
        return depositoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ GET: buscar depósitos por barrio
    @GetMapping("/barrio/{nombreBarrio}")
    public ResponseEntity<List<Deposito>> obtenerPorBarrio(@PathVariable String nombreBarrio) {
        return ResponseEntity.ok(depositoService.obtenerPorBarrio(nombreBarrio));
    }

    // ✅ GET: buscar depósitos por localidad
    @GetMapping("/localidad/{nombreLocalidad}")
    public ResponseEntity<List<Deposito>> obtenerPorLocalidad(@PathVariable String nombreLocalidad) {
        return ResponseEntity.ok(depositoService.obtenerPorLocalidad(nombreLocalidad));
    }

    // ✅ POST: crear depósito (solo ADMIN)
    @PostMapping
    public ResponseEntity<Deposito> crear(@RequestBody Deposito deposito) {
        return ResponseEntity.ok(depositoService.guardar(deposito));
    }

    // ✅ PUT: actualizar depósito (solo ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<Deposito> actualizar(@PathVariable Long id, @RequestBody Deposito deposito) {
        return depositoService.obtenerPorId(id)
                .map(d -> {
                    deposito.setIdDeposito(id);
                    return ResponseEntity.ok(depositoService.guardar(deposito));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE: eliminar depósito (solo ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        depositoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
