package ar.edu.utnfc.backend.atencion.controller;

import ar.edu.utnfc.backend.atencion.model.Contenedor;
import ar.edu.utnfc.backend.atencion.service.ContenedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenedores")
public class ContenedorController {

    private final ContenedorService service;

    public ContenedorController(ContenedorService service) {
        this.service = service;
    }

    // ==========================================================
    // 🔹 Listar TODOS los contenedores (opcional)
    // ==========================================================
    @GetMapping
    public List<Contenedor> listar() {
        return service.listar();
    }

    // ==========================================================
    // 🔹 Crear contenedor — versión genérica
    // ==========================================================
    @PostMapping
    public Contenedor crear(@RequestBody Contenedor contenedor) {
        return service.crear(contenedor);
    }

    // ==========================================================
    // 🔹 Buscar contenedor por ID
    // ==========================================================
    @GetMapping("/{id}")
    public Contenedor buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    // ==========================================================
    // 🔥 RF1 OFICIAL TPI:
    // GET /contenedores/cliente/{idCliente}
    // ==========================================================
    @GetMapping("/cliente/{idCliente}")
    public List<Contenedor> listarPorCliente(@PathVariable Long idCliente) {
        return service.listarPorCliente(idCliente);
    }

    // ==========================================================
    // 🔥 RF1 OFICIAL TPI:
    // POST /contenedores/cliente/{idCliente}
    // ==========================================================
    @PostMapping("/cliente/{idCliente}")
    public Contenedor crearParaCliente(
            @PathVariable Long idCliente,
            @RequestBody Contenedor contenedor) {

        return service.crearParaCliente(idCliente, contenedor);
    }

    // ==========================================================
    // 🔹 Eliminar contenedor
    // ==========================================================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // ==========================================================
    // 🔥 RNF2 — Actualizar estado del contenedor
    // PUT /api/contenedores/{id}/estado?idEstado=#
    // ==========================================================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestParam Long idEstado
    ) {
        service.actualizarEstado(id, idEstado);
        return ResponseEntity.ok("Estado actualizado");
    }

    // ==========================================================
    // 🔥 RNF2 — Obtener estado del contenedor
    // GET /api/contenedores/{id}/estado
    // ==========================================================
    @GetMapping("/{id}/estado")
    public ResponseEntity<?> obtenerEstado(@PathVariable Long id) {
        var estado = service.obtenerEstado(id);
        return ResponseEntity.ok(estado);
    }

    // ==========================================================
    // ⭐⭐⭐ RF5 — Contenedores pendientes de entrega ⭐⭐⭐
    // ==========================================================
    @GetMapping("/pendientes")
    public ResponseEntity<?> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }
}
