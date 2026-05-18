package ar.edu.utnfc.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleMapsService {

    @Value("${google.api.base-url}")
    private String baseUrl;

    @Value("${google.api.key}")
    private String apiKey;

    private final RestClient restClient;

    /**
     * 🔹 Devuelve la respuesta completa de la API de Google Maps (modo texto)
     *    — útil para probar manualmente el JSON completo.
     */
    public String consultarDistancia(String origenLat, String origenLng,
                                     String destinoLat, String destinoLng) {

        String origen = origenLat + "," + origenLng;
        String destino = destinoLat + "," + destinoLng;

        String url = baseUrl + "?origins=" + origen +
                     "&destinations=" + destino +
                     "&key=" + apiKey;

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    /**
     * 🔹 Consulta a la API Distance Matrix de Google y devuelve el tiempo estimado (en minutos).
     */
    public Integer consultarTiempo(String origenLat, String origenLng,
                                   String destinoLat, String destinoLng) {
        try {
            String origen = origenLat + "," + origenLng;
            String destino = destinoLat + "," + destinoLng;

            String url = baseUrl + "?origins=" + origen +
                         "&destinations=" + destino +
                         "&key=" + apiKey;

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            // Convertimos el JSON de respuesta en un mapa
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response, Map.class);

            // Navegamos el JSON para obtener "duration.value" (segundos)
            List<?> rows = (List<?>) json.get("rows");
            if (rows == null || rows.isEmpty()) return null;

            Map<?, ?> firstRow = (Map<?, ?>) rows.get(0);
            List<?> elements = (List<?>) firstRow.get("elements");
            if (elements == null || elements.isEmpty()) return null;

            Map<?, ?> firstElement = (Map<?, ?>) elements.get(0);
            Map<?, ?> duration = (Map<?, ?>) firstElement.get("duration");
            if (duration == null) return null;

            int segundos = (int) duration.get("value");
            int minutos = segundos / 60;

            System.out.println("⏱️ Tiempo estimado real según Google Maps: " + minutos + " minutos");
            return minutos;

        } catch (Exception e) {
            System.out.println("❌ Error al consultar Google Maps API: " + e.getMessage());
            return null;
        }
    }
}
