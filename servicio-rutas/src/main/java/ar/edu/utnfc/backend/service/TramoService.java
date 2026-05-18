package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.model.Estado;
import ar.edu.utnfc.backend.model.Tramo;
import ar.edu.utnfc.backend.repository.EstadoRepository;
import ar.edu.utnfc.backend.repository.TramoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TramoService {

    private final EstadoRepository estadoRepository;
    private final TramoRepository tramoRepository;

    // ==========================================================
    // CRUD
    // ==========================================================
    public List<Tramo> listarTramos() {
        return tramoRepository.findAll();
    }

    public Optional<Tramo> obtenerPorId(Long id) {
        return tramoRepository.findById(id);
    }

    public Tramo guardar(Tramo tramo) {

        // Estado inicial obligatorio para un tramo nuevo
        Estado estimado = estadoRepository
                .findByNombreAndAmbito("Estimado", "TRAMO")
                .orElseThrow(() -> new RuntimeException("Estado Estimado/TRAMO no encontrado"));

        tramo.setEstado(estimado);

        return tramoRepository.save(tramo);
    }

    public void eliminar(Long id) {
        tramoRepository.deleteById(id);
    }

        public Tramo actualizar(Long id, Tramo tramoNuevo) {
        return tramoRepository.findById(id)
                .map(actual -> {
                    actual.setCostoAproximado(tramoNuevo.getCostoAproximado());
                    actual.setCostoReal(tramoNuevo.getCostoReal());
                    actual.setFechaHoraInicio(tramoNuevo.getFechaHoraInicio());
                    actual.setFechaHoraFin(tramoNuevo.getFechaHoraFin());
                    actual.setIdCamion(tramoNuevo.getIdCamion());
                    actual.setIdTipoTramo(tramoNuevo.getIdTipoTramo());
                    actual.setIdDepositoOrigen(tramoNuevo.getIdDepositoOrigen());
                    actual.setIdDepositoDestino(tramoNuevo.getIdDepositoDestino());
                    actual.setRuta(tramoNuevo.getRuta());
                    return tramoRepository.save(actual);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));
    }

    // ==========================================================
    // RF7 - INICIAR TRAMO
    // ==========================================================
    public void iniciarTramo(Long idTramo) {

        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        // Validar transición: solo se puede iniciar si está ASIGNADO
        if (!tramo.getEstado().getNombre().equalsIgnoreCase("Asignado")) {
            throw new RuntimeException("El tramo solo puede iniciarse si está en estado Asignado");
        }

        Estado iniciado = estadoRepository
                .findByNombreAndAmbito("Iniciado", "TRAMO")
                .orElseThrow(() -> new RuntimeException("Estado Iniciado/TRAMO no encontrado"));

        tramo.setEstado(iniciado);
        tramo.setFechaHoraInicio(LocalDateTime.now());

        tramoRepository.save(tramo);
    }

    // ==========================================================
    // RF7 - FINALIZAR TRAMO
    // ==========================================================
    public void finalizarTramo(Long idTramo) {

        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        // Validar transición: solo se puede finalizar si está Iniciado
        if (!tramo.getEstado().getNombre().equalsIgnoreCase("Iniciado")) {
            throw new RuntimeException("El tramo solo puede finalizarse si está en estado Iniciado");
        }

        Estado finalizado = estadoRepository
                .findByNombreAndAmbito("Finalizado", "TRAMO")
                .orElseThrow(() -> new RuntimeException("Estado Finalizado/TRAMO no encontrado"));

        tramo.setEstado(finalizado);
        tramo.setFechaHoraFin(LocalDateTime.now());

        tramoRepository.save(tramo);
    }
}
