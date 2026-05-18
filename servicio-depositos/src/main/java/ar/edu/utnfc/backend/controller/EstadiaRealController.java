package ar.edu.utnfc.backend.controller;

import ar.edu.utnfc.backend.model.EstadiaReal;
import ar.edu.utnfc.backend.service.EstadiaRealService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estadias")
@CrossOrigin(origins = "*")
public class EstadiaRealController {

    private final EstadiaRealService service;

    public EstadiaRealController(EstadiaRealService service) {
        this.service = service;
    }

    // ================================================
    // ENTRADA A DEPÓSITO
    // ================================================
    @PostMapping("/entrada")
    public EstadiaReal registrarEntrada(@RequestBody Map<String, Object> body) {

        Long idContenedor = Long.valueOf(body.get("idContenedor").toString());
        Long idDeposito = Long.valueOf(body.get("idDeposito").toString());
        LocalDateTime fechaEntrada = LocalDateTime.parse(body.get("fechaEntrada").toString());

        return service.registrarEntrada(idContenedor, idDeposito, fechaEntrada);
    }

    // ================================================
    // SALIDA DE DEPÓSITO
    // ================================================
    @PutMapping("/salida")
    public EstadiaReal registrarSalida(@RequestBody Map<String, Object> body) {

        Long idContenedor = Long.valueOf(body.get("idContenedor").toString());
        Long idDeposito = Long.valueOf(body.get("idDeposito").toString());
        LocalDateTime fechaSalida = LocalDateTime.parse(body.get("fechaSalida").toString());

        return service.registrarSalida(idContenedor, idDeposito, fechaSalida);
    }

    // ================================================
    // LISTAR ESTADIAS POR CONTENEDOR
    // ================================================
    @GetMapping("/contenedor/{idContenedor}")
    public List<EstadiaReal> listarPorContenedor(@PathVariable Long idContenedor) {
        return service.listarPorContenedor(idContenedor);
    }
}
