package ar.edu.utnfc.backend.tarifas.client;


import ar.edu.utnfc.backend.tarifas.client.dto.DepositoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DepositoClient {

    private final RestClient restClient;

    @Value("${servicios.depositos.url}")
    private String baseUrl;

    public DepositoClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public DepositoDTO obtenerDeposito(Long idDeposito) {
        return restClient.get()
                .uri(baseUrl + "/depositos/" + idDeposito)
                .retrieve()
                .body(DepositoDTO.class);
    }
}

