package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.model.EstadiaReal;
import ar.edu.utnfc.backend.repository.EstadiaRealRepository;

import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EstadiaRealService {

    private final EstadiaRealRepository repo;

    public EstadiaRealService(EstadiaRealRepository repo) {
        this.repo = repo;
    }

    // ================================
    // REGISTRAR ENTRADA
    // ================================
    public EstadiaReal registrarEntrada(Long idContenedor, Long idDeposito, java.time.LocalDateTime fechaEntrada) {

        EstadiaReal estadia = EstadiaReal.builder()
                .idContenedor(idContenedor)
                .idDeposito(idDeposito)
                .fechaEntrada(fechaEntrada)
                .build();

        return repo.save(estadia);
    }

    // ================================
    // REGISTRAR SALIDA
    // ================================
    public EstadiaReal registrarSalida(Long idContenedor, Long idDeposito, java.time.LocalDateTime fechaSalida) {

        EstadiaReal estadia = repo.findFirstByIdContenedorAndIdDepositoAndFechaSalidaIsNull(
                idContenedor,
                idDeposito
        );

        if (estadia == null)
            throw new RuntimeException("No existe estadía abierta para este contenedor y depósito");

        estadia.setFechaSalida(fechaSalida);

        // cálculo exacto de días completos
        long dias = ChronoUnit.DAYS.between(estadia.getFechaEntrada(), fechaSalida);

        if (dias == 0)
            dias = 1; // regla común: 1 día mínimo

        estadia.setDias(dias);

        return repo.save(estadia);
    }

    // ================================
    // LISTAR ESTADÍAS POR CONTENEDOR
    // ================================
    public List<EstadiaReal> listarPorContenedor(Long idContenedor) {
        return repo.findByIdContenedor(idContenedor);
    }
}
