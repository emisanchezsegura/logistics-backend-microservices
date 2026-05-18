package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.client.DepositosApiClient;
import ar.edu.utnfc.backend.client.TransporteApiClient;
import ar.edu.utnfc.backend.dto.*;
import ar.edu.utnfc.backend.model.Estado;
import ar.edu.utnfc.backend.model.Ruta;
import ar.edu.utnfc.backend.model.Tramo;
import ar.edu.utnfc.backend.repository.EstadoRepository;
import ar.edu.utnfc.backend.repository.RutaRepository;
import ar.edu.utnfc.backend.repository.TramoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;
    private final TransporteApiClient transporteClient;
    private final TramoRepository tramoRepo;
    private final EstadoRepository estadoRepository;
    private final DepositosApiClient depositosApiClient;
    private final GoogleMapsService googleMapsService;

    // ==========================================================
    // CRUD
    // ==========================================================

    public List<Ruta> listarRutas() {
        return rutaRepository.findAll();
    }

    public Optional<Ruta> obtenerPorId(Long id) {
        return rutaRepository.findById(id);
    }

    public Ruta guardar(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public void eliminar(Long id) {
        rutaRepository.deleteById(id);
    }

    // ==========================================================
    // TRAMOS DE LA RUTA (NECESARIO PARA ATENCIÓN)
    // ==========================================================

    public List<Tramo> obtenerTramosDeRuta(Long idRuta) {
        return tramoRepo.findByRutaIdRuta(idRuta);
    }

    // ==========================================================
    // ASIGNAR CAMIÓN  ✔ CORREGIDO
    // ==========================================================

    public void asignarCamion(Long idTramo, Long idCamion) {

        Tramo tramo = tramoRepo.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        tramo.setIdCamion(idCamion);

        // *** CORREGIDO *** → coincide con tu BD ("Asignado")
        Estado asignado = estadoRepository
                .findByNombreAndAmbito("Asignado", "TRAMO")
                .orElseThrow(() -> new RuntimeException("Estado Asignado/TRAMO no encontrado"));

        tramo.setEstado(asignado);

        tramoRepo.save(tramo);
    }

    // ==========================================================
    // CONSUMO PROMEDIO
    // ==========================================================

    public Double calcularConsumoPromedio(Long idRuta) {

        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        List<Tramo> tramos = obtenerTramosDeRuta(idRuta);

        double suma = 0;
        int cantidad = 0;

        for (Tramo tramo : tramos) {
            if (tramo.getIdCamion() != null) {
                CamionDTO camion = transporteClient.obtenerCamionPorId(tramo.getIdCamion());

                if (camion.getConsumoCombustibleKm() != null) {
                    suma += camion.getConsumoCombustibleKm();
                    cantidad++;
                }
            }
        }

        return cantidad == 0 ? 0.0 : suma / cantidad;
    }

    // ==========================================================
    // RNF3 - RUTAS TENTATIVAS
    // ==========================================================

    private double distanciaRapida(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    private double distanciaRealKm(double lat1, double lon1, double lat2, double lon2) {

        try {
            String json = googleMapsService.consultarDistancia(
                    String.valueOf(lat1), String.valueOf(lon1),
                    String.valueOf(lat2), String.valueOf(lon2)
            );

            var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map jsonMap = mapper.readValue(json, Map.class);

            List rows = (List) jsonMap.get("rows");
            Map firstRow = (Map) rows.get(0);
            List elements = (List) firstRow.get("elements");
            Map distance = (Map) ((Map) elements.get(0)).get("distance");

            double metros = Double.parseDouble(distance.get("value").toString());
            return metros / 1000.0;

        } catch (Exception e) {
            return distanciaRapida(lat1, lon1, lat2, lon2);
        }
    }

    private TramoTentativoDTO calcularTramo(
            Double lat1, Double lon1,
            Double lat2, Double lon2,
            Long idDepOrigen,
            Long idDepDestino
    ) {

        Integer tiempoMin = googleMapsService.consultarTiempo(
                lat1.toString(), lon1.toString(),
                lat2.toString(), lon2.toString()
        );

        double distanciaKm = distanciaRealKm(lat1, lon1, lat2, lon2);
        double costoTentativo = distanciaKm * 1.2;

        return new TramoTentativoDTO(
                idDepOrigen,
                idDepDestino,
                distanciaKm,
                tiempoMin,
                costoTentativo
        );
    }

    public List<RutaTentativaDTO> calcularRutasTentativas(RutaTentativaRequestDTO req) {

        List<DepositoDTO> depositos = depositosApiClient.obtenerTodos();

        depositos.sort(Comparator.comparingDouble(
                d -> distanciaRapida(req.getLatOrigen(), req.getLonOrigen(),
                        d.getLatitud(), d.getLongitud())
        ));

        List<DepositoDTO> cercanos = depositos.stream().limit(2).toList();

        List<RutaTentativaDTO> rutas = new ArrayList<>();

        for (DepositoDTO d : cercanos) {

            List<TramoTentativoDTO> tramos = new ArrayList<>();

            tramos.add(calcularTramo(
                    req.getLatOrigen(), req.getLonOrigen(),
                    d.getLatitud(), d.getLongitud(),
                    null, d.getIdDeposito()
            ));

            tramos.add(calcularTramo(
                    d.getLatitud(), d.getLongitud(),
                    req.getLatDestino(), req.getLonDestino(),
                    d.getIdDeposito(), null
            ));

            rutas.add(armarRutaTentativa(tramos));
        }

        return rutas;
    }

    private RutaTentativaDTO armarRutaTentativa(List<TramoTentativoDTO> tramos) {

        double distanciaTotal = tramos.stream().mapToDouble(TramoTentativoDTO::getDistanciaKm).sum();
        int tiempoTotal = tramos.stream().mapToInt(TramoTentativoDTO::getTiempoMin).sum();
        double costoTotal = tramos.stream().mapToDouble(TramoTentativoDTO::getCostoTentativo).sum();

        return new RutaTentativaDTO(distanciaTotal, tiempoTotal, costoTotal, tramos);
    }

    // ==========================================================
    // CREAR RUTA REAL DESDE TENTATIVA
    // ==========================================================

    public Ruta crearDesdeTentativa(CrearRutaDesdeTentativaDTO dto) {

        Ruta ruta = new Ruta();
        ruta.setCantidadTramos(dto.getTramos().size());

        long cantDepositos = dto.getTramos().stream()
                .flatMap(t -> Arrays.stream(new Long[]{t.getIdDepositoOrigen(), t.getIdDepositoDestino()}))
                .filter(Objects::nonNull)
                .distinct()
                .count();

        ruta.setCantidadDepositos((int) cantDepositos);

        ruta = rutaRepository.save(ruta);

        for (TramoTentativoDTO t : dto.getTramos()) {

            Tramo tramo = new Tramo();
            tramo.setRuta(ruta);
            tramo.setCostoAproximado(t.getCostoTentativo());
            tramo.setCostoReal(null);
            tramo.setIdDepositoOrigen(t.getIdDepositoOrigen());
            tramo.setIdDepositoDestino(t.getIdDepositoDestino());

            Estado est = estadoRepository.findByNombreAndAmbito("Estimado", "TRAMO")
                    .orElseThrow(() -> new RuntimeException("Estado ESTIMADO/TRAMO no encontrado"));

            tramo.setEstado(est);

            tramoRepo.save(tramo);
        }

        return ruta;
    }
}
