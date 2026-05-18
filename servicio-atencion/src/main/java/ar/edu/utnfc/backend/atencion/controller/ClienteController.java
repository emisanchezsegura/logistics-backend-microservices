package ar.edu.utnfc.backend.atencion.controller;

import ar.edu.utnfc.backend.atencion.model.Cliente;
import ar.edu.utnfc.backend.atencion.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // ==========================================================
    // 🔹 Listar todos los clientes
    // ==========================================================
    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    // ==========================================================
    // 🔹 Crear cliente
    // ==========================================================
    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return service.crear(cliente);
    }

    // ==========================================================
    // 🔹 Buscar cliente por ID
    // ==========================================================
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    // ==========================================================
    // 🔹 Eliminar cliente
    // ==========================================================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // ==========================================================
    // 🔥 Actualizar solo el teléfono del cliente (RF TPI)
    // Body esperado:
    // { "telefono": "3517770000" }
    // ==========================================================
    @PutMapping("/{id}/telefono")
    public Cliente actualizarTelefono(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String nuevoTelefono = body.get("telefono");
        return service.actualizarTelefono(id, nuevoTelefono);
    }
}
