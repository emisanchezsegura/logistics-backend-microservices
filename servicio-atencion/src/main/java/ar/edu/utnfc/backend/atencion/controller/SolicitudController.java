package ar.edu.utnfc.backend.atencion.controller;

import ar.edu.utnfc.backend.atencion.dto.SolicitudRequestDTO;
import ar.edu.utnfc.backend.atencion.model.Solicitud;
import ar.edu.utnfc.backend.atencion.service.SolicitudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    // ==========================================================
    // 🔹 Obtener todas las solicitudes
    // ==========================================================
    @GetMapping
    public List<Solicitud> listar() {
        return service.listar();
    }

    // ==========================================================
    // 🔹 Crear nueva solicitud
    // ==========================================================
    @PostMapping
    public Solicitud crear(@RequestBody SolicitudRequestDTO dto) {
        return service.crearDesdeDTO(dto);
    }

    // ==========================================================
    // 🔹 Buscar solicitud por ID
    // ==========================================================
    @GetMapping("/{id}")
    public Solicitud buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    // ==========================================================
    // 🔹 Eliminar solicitud
    // ==========================================================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // ==========================================================
    // 🔹 Cambiar estado manual
    // ==========================================================
    @PutMapping("/{idSolicitud}/estado/{idEstado}")
    public Solicitud cambiarEstado(
            @PathVariable Long idSolicitud,
            @PathVariable Long idEstado) {

        return service.cambiarEstado(idSolicitud, idEstado);
    }

    // ==========================================================
    // 🔹 Asignar ruta → ESTADO = PROGRAMADA
    // ==========================================================
    @PutMapping("/{idSolicitud}/asignar-ruta/{idRuta}")
    public Solicitud asignarRuta(
            @PathVariable Long idSolicitud,
            @PathVariable Long idRuta) {

        return service.asignarRuta(idSolicitud, idRuta);
    }

    // ==========================================================
    // 🔹 Iniciar tránsito → ESTADO = EN TRÁNSITO
    // ==========================================================
    @PutMapping("/{idSolicitud}/iniciar-transito")
    public Solicitud iniciarTransito(@PathVariable Long idSolicitud) {
        return service.iniciarTransito(idSolicitud);
    }

    // ==========================================================
    // 🔹 Finalizar → ESTADO = ENTREGADA
    // ==========================================================
    @PutMapping("/{idSolicitud}/finalizar")
    public Solicitud finalizar(@PathVariable Long idSolicitud) {
        return service.finalizar(idSolicitud);
    }

    // ==========================================================
    // ⭐⭐⭐ RF6 — Asignar camión a un tramo ⭐⭐⭐
    // ==========================================================
    @PutMapping("/{idSolicitud}/asignar-camion")
    public Solicitud asignarCamion(
            @PathVariable Long idSolicitud,
            @RequestParam Long idTramo,
            @RequestParam Long idCamion
    ) {
        return service.asignarCamion(idSolicitud, idTramo, idCamion);
    }

    // ==========================================================
    // PASO 4 — NOTIFICACIÓN DESDE RUTAS (INICIO DE TRAMO)
    // ==========================================================
    @PutMapping("/notificar-inicio-tramo")
    public void notificarInicioTramo(
            @RequestParam Long idSolicitud,
            @RequestParam Long idTramo
    ) {
        service.notificarInicioTramo(idSolicitud, idTramo);
    }

    // ==========================================================
    // PASO 4 — NOTIFICACIÓN DESDE RUTAS (FIN DE TRAMO)
    // ==========================================================
    @PutMapping("/notificar-fin-tramo")
    public void notificarFinTramo(
            @RequestParam Long idSolicitud,
            @RequestParam Long idTramo
    ) {
        service.notificarFinTramo(idSolicitud, idTramo);
    }

        // ==========================================================
    // PASO 8 — CALCULAR COSTO FINAL (RF8)
    // ==========================================================
    @PutMapping("/{idSolicitud}/calcular-costo-final")
    public Solicitud calcularCostoFinal(@PathVariable Long idSolicitud) {
        return service.calcularCostoFinal(idSolicitud);
    }

}
