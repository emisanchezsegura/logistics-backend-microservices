package ar.edu.utnfc.backend.atencion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TarifaClient {

    @Value("${servicios.tarifas.url}")
    private String tarifasUrl; // ejemplo: http://servicio-tarifas:8086

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Llama al microservicio Tarifas para calcular el costo estimado
     * según el tiempo de viaje y una tarifa específica.
     */
    public Double obtenerCostoEstimado(Double tiempoMinutos, Long idTarifa) {
        try {
            String url = tarifasUrl + "/api/tarifas/costo?tiempoMinutos=" + tiempoMinutos + "&idTarifa=" + idTarifa;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("costoEstimado")) {
                Double costo = Double.valueOf(response.get("costoEstimado").toString());
                System.out.println("💰 [Atención] Costo estimado recibido: " + costo);
                return costo;
            }
        } catch (Exception e) {
            System.err.println("⚠️ [Atención] Error al consultar servicio Tarifas: " + e.getMessage());
        }

        return null;
    }

        public Map<String, Object> obtenerTarifaPorId(Long idTarifa) {
        String url = tarifasUrl + "/api/tarifas/" + idTarifa;
        return restTemplate.getForObject(url, Map.class);
    }
}
