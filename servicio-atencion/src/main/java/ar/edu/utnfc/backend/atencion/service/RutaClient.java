package ar.edu.utnfc.backend.atencion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class RutaClient {

    @Value("${servicios.rutas.url}")
    private String rutasUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // ==========================================================
    // 🔹 Obtener tiempo estimado (Google Maps)
    // ==========================================================
    public Integer obtenerTiempoEstimado(String latOrigen, String lonOrigen,
                                         String latDestino, String lonDestino) {

        try {
            String url = rutasUrl +
                    "/api/rutas/tiempo?latOrigen=" + latOrigen +
                    "&lonOrigen=" + lonOrigen +
                    "&latDestino=" + latDestino +
                    "&lonDestino=" + lonDestino;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("tiempoEstimado")) {
                Object valor = response.get("tiempoEstimado");
                if (valor instanceof Number)
                    return ((Number) valor).intValue();
            }

        } catch (Exception e) {
            System.err.println("❌ Error servicio Rutas: " + e.getMessage());
        }

        return null;
    }

    // ==========================================================
    // 🔹 Obtener ruta por ID (usado en RF4 y en validaciones)
    // ==========================================================
    public Map<String, Object> obtenerRutaPorId(Long idRuta) {
        try {
            String url = rutasUrl + "/api/rutas/" + idRuta;

            return restTemplate.getForObject(url, Map.class);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error obteniendo ruta " + idRuta + ": " + e.getMessage());
        }
    }

    // ==========================================================
    // ⭐⭐⭐ RF6 — Asignar Camión a un Tramo ⭐⭐⭐
    // ==========================================================
    public void asignarCamion(Long idRuta, Long idTramo, Long idCamion) {

        try {
            String url = rutasUrl + "/api/rutas/" + idRuta + "/asignarCamion";

            Map<String, Object> body = new HashMap<>();
            body.put("idTramo", idTramo);
            body.put("idCamion", idCamion);

            restTemplate.put(url, body);

        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Error al asignar camión en Ruta " + idRuta +
                    ": " + e.getMessage()
            );
        }
    }

    // ===============================================
    // OBTENER TRAMO POR ID (PASO 2A)
    // ===============================================
    public Map<String, Object> obtenerTramoPorId(Long idTramo) {
        try {
            String url = rutasUrl + "/api/tramos/" + idTramo;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("❌ Error obteniendo tramo " + idTramo + ": " + e.getMessage());
        }
    }


}
