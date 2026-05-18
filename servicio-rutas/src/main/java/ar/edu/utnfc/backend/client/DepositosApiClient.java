package ar.edu.utnfc.backend.client;

import ar.edu.utnfc.backend.dto.DepositoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DepositosApiClient {

    private final RestClient depositosRestClient;

    public List<DepositoDTO> obtenerTodos() {

        DepositoDTO[] depositos = depositosRestClient
                .get()
                .uri("/depositos")   // GET http://servicio-depositos:8085/api/depositos
                .retrieve()
                .body(DepositoDTO[].class);

        return Arrays.asList(depositos);
    }
}
