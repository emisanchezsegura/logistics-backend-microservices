package ar.edu.utnfc.backend.tarifas.controller;

import ar.edu.utnfc.backend.tarifas.model.Tarifa;
import ar.edu.utnfc.backend.tarifas.service.TarifaService;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tarifas")
@CrossOrigin(origins = "*")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    // ============================================================
    // ======================= CRUD TARIFAS =========================
    // ============================================================

    @GetMapping
    public List<Tarifa> getAll() {
        return tarifaService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Tarifa> getById(@PathVariable Long id) {
        return tarifaService.getById(id);
    }

    @PostMapping
    public Tarifa create(@RequestBody Tarifa tarifa) {
        return tarifaService.create(tarifa);
    }

    @PutMapping("/{id}")
    public Tarifa update(@PathVariable Long id, @RequestBody Tarifa tarifa) {
        return tarifaService.update(id, tarifa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tarifaService.delete(id);
    }


    // ============================================================
    // ======= CÁLCULO APROXIMADO DE COSTO (SE CONSERVA) ===========
    // ============================================================

    @GetMapping("/costo")
    public Map<String, Object> calcularCosto(
            @RequestParam Double tiempoMinutos,
            @RequestParam Long idTarifa
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        double costo = tarifaService.calcularCosto(tiempoMinutos, idTarifa);

        respuesta.put("costoEstimado", costo);
        respuesta.put("tiempoMinutos", tiempoMinutos);
        respuesta.put("tarifaUsada", idTarifa);

        return respuesta;
    }
}
