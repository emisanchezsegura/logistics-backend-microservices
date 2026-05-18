package ar.edu.utnfc.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TransporteClientConfig {

    @Bean
    RestClient transporteRestClient(@Value("${app.transporte.url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}