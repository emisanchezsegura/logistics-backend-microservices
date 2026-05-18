package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.Ruta;
import ar.edu.utnfc.backend.model.Tramo;
import ar.edu.utnfc.backend.service.GoogleMapsService;
import ar.edu.utnfc.backend.service.RutaService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.utnfc.backend.dto.AsignarCamionRequest;
import ar.edu.utnfc.backend.dto.RutaTentativaRequestDTO;
import ar.edu.utnfc.backend.dto.RutaTentativaDTO;
import ar.edu.utnfc.backend.dto.CrearRutaDesdeTentativaDTO;

import java.util.*;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;
    private final GoogleMapsService googleMapsService;

    // ==========================================================
    // 🔥 NUEVO → LISTAR TODAS LAS RUTAS (NECESARIO PARA ATENCIÓN)
    // ==========================================================
    @GetMapping
    public ResponseEntity<List<Ruta>> listarRutas() {
        return ResponseEntity.ok(rutaService.listarRutas());
    }

    // ==========================================================
    // GET RUTA + TRAMOS (CORREGIDO)
    // ==========================================================
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {

        Ruta ruta = rutaService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        List<Tramo> tramos = rutaService.obtenerTramosDeRuta(id);

        Map<String, Object> response = new HashMap<>();
        response.put("idRuta", ruta.getIdRuta());
        response.put("cantidadTramos", ruta.getCantidadTramos());
        response.put("cantidadDepositos", ruta.getCantidadDepositos());
        response.put("tramos", tramos);

        return ResponseEntity.ok(response);
    }

    // ==========================================================
    // CRUD BÁSICO
    // ==========================================================
    @PostMapping
    public ResponseEntity<Ruta> crear(@RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.guardar(ruta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rutaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================================
    // GOOGLE MAPS
    // ==========================================================
    @GetMapping("/tiempo")
    public ResponseEntity<?> obtenerTiempoEstimado(
            @RequestParam double latOrigen,
            @RequestParam double lonOrigen,
            @RequestParam double latDestino,
            @RequestParam double lonDestino
    ) {
        Integer tiempoEstimado = googleMapsService.consultarTiempo(
                String.valueOf(latOrigen),
                String.valueOf(lonOrigen),
                String.valueOf(latDestino),
                String.valueOf(lonDestino)
        );

        if (tiempoEstimado == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo calcular tiempo estimado"));
        }

        return ResponseEntity.ok(Map.of("tiempoEstimado", tiempoEstimado));
    }

    // ==========================================================
    // ASIGNAR CAMIÓN A TRAMO
    // ==========================================================
    @PutMapping("/{id}/asignarCamion")
    public ResponseEntity<?> asignarCamion(
            @PathVariable Long id,
            @RequestBody AsignarCamionRequest req
    ) {
        rutaService.asignarCamion(req.getIdTramo(), req.getIdCamion());
        return ResponseEntity.ok("Camión asignado");
    }

    // ==========================================================
    // CONSUMO PROMEDIO
    // ==========================================================
    @GetMapping("/{id}/consumoPromedio")
    public ResponseEntity<Double> obtenerConsumoPromedio(@PathVariable Long id) {
        return ResponseEntity.ok(rutaService.calcularConsumoPromedio(id));
    }

    // ==========================================================
    // RUTAS TENTATIVAS
    // ==========================================================
    @PostMapping("/tentativas")
    public ResponseEntity<List<RutaTentativaDTO>> calcularRutasTentativas(
            @RequestBody RutaTentativaRequestDTO request
    ) {
        return ResponseEntity.ok(rutaService.calcularRutasTentativas(request));
    }

    // ==========================================================
    // CREAR RUTA REAL DESDE TENTATIVA
    // ==========================================================
    @PostMapping("/crear-desde-tentativa")
    public ResponseEntity<Ruta> crearDesdeTentativa(@RequestBody CrearRutaDesdeTentativaDTO dto) {
        return ResponseEntity.ok(rutaService.crearDesdeTentativa(dto));
    }
}
