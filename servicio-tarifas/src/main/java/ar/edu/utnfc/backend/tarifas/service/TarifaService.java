package ar.edu.utnfc.backend.tarifas.service;

import ar.edu.utnfc.backend.tarifas.model.Tarifa;
import ar.edu.utnfc.backend.tarifas.repository.TarifaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    // ============================================================
    // ======================= CRUD TARIFAS =========================
    // ============================================================

    public List<Tarifa> getAll() {
        return tarifaRepository.findAll();
    }

    public Optional<Tarifa> getById(Long id) {
        return tarifaRepository.findById(id);
    }

    public Tarifa create(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    public Tarifa update(Long id, Tarifa tarifa) {
        return tarifaRepository.findById(id)
                .map(existing -> {
                    tarifa.setIdTarifa(id);
                    return tarifaRepository.save(tarifa);
                })
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con id " + id));
    }

    public void delete(Long id) {
        tarifaRepository.deleteById(id);
    }


    // ============================================================
    // ======= CÁLCULO APROXIMADO DE COSTO (SE CONSERVA) ===========
    // ============================================================

    public double calcularCosto(double tiempoMinutos, Long idTarifa) {

        Tarifa tarifa = tarifaRepository.findById(idTarifa)
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada con id " + idTarifa));

        double costoBase = tarifa.getCostoKmBase() != null ? tarifa.getCostoKmBase() : 100.0;
        double valorLitro = tarifa.getValorLitroCombustible() != null ? tarifa.getValorLitroCombustible() : 50.0;
        double gestion = tarifa.getCostoGestionTramo() != null ? tarifa.getCostoGestionTramo() : 20.0;

        double horas = tiempoMinutos / 60.0;

        // Fórmula básica: se permite en enunciado (es aproximada)
        double costoTotal = (horas * costoBase) + gestion + (valorLitro * 0.1);

        return Math.round(costoTotal * 100.0) / 100.0;
    }
}
