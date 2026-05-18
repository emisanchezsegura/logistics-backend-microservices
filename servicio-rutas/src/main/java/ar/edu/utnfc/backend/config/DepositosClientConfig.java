package ar.edu.utnfc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DepositosClientConfig {

    @Bean
    RestClient depositosRestClient() {
        return RestClient.builder()
                .baseUrl("http://servicio-depositos:8085/api")
                .build();
    }
}
