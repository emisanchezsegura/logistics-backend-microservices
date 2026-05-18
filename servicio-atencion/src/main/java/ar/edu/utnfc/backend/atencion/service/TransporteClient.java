package ar.edu.utnfc.backend.atencion.service;

import ar.edu.utnfc.backend.atencion.dto.CamionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

@Service
public class TransporteClient {

    @Value("${servicios.transporte.url}")
    private String transporteUrl; // configurable desde application.yml

    private final RestTemplate restTemplate = new RestTemplate();

    // ==========================================================
    // 🔹 Obtener camiones disponibles según peso y volumen
    // ==========================================================
    public List<CamionDTO> obtenerCamionesDisponibles(Double peso, Double volumen) {
        String url = transporteUrl + "/api/camiones/disponibles?peso=" + peso + "&volumen=" + volumen;
        CamionDTO[] respuesta = restTemplate.getForObject(url, CamionDTO[].class);
        return Arrays.asList(respuesta);
    }

    // ==========================================================
    // 🔹 Obtener un camión por su ID (necesario para RF6)
    // ==========================================================
    public CamionDTO obtenerCamionPorId(Long idCamion) {
        String url = transporteUrl + "/api/camiones/" + idCamion;
        return restTemplate.getForObject(url, CamionDTO.class);
    }
}
