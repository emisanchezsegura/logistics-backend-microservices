package ar.edu.utnfc.backend.client;

import ar.edu.utnfc.backend.dto.CamionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class TransporteApiClient {

    private final RestClient transporteRestClient;

    public CamionDTO obtenerCamionPorId(Long idCamion) {
        return transporteRestClient.get()
                .uri("/camiones/{id}", idCamion)
                .retrieve()
                .body(CamionDTO.class);
    }
}