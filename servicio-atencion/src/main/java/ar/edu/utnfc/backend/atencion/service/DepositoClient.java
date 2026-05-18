package ar.edu.utnfc.backend.atencion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepositoClient {

    // Base para CRUD de depósitos
    @Value("${servicios.depositos.url}")
    private String depositosUrl;

    // Base para operaciones de estadías reales
    @Value("${servicios.estadias.url}")
    private String estadiasUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // ================================
    // OBTENER DEPÓSITO
    // ================================
    public Map<String, Object> obtenerDepositoPorId(Long idDeposito) {
        try {
            String url = depositosUrl + "/" + idDeposito;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("❌ Error obteniendo depósito " + idDeposito + ": " + e.getMessage());
        }
    }

    // ================================
    // REGISTRAR ENTRADA
    // ================================
    public void registrarEntrada(Long idContenedor, Long idDeposito, LocalDateTime fechaEntrada) {
        try {
            String url = estadiasUrl + "/entrada";

            Map<String, Object> body = new HashMap<>();
            body.put("idContenedor", idContenedor);
            body.put("idDeposito", idDeposito);
            body.put("fechaEntrada", fechaEntrada.toString());

            restTemplate.postForObject(url, body, Map.class);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error registrando entrada de estadía: " + e.getMessage());
        }
    }

    // ================================
    // REGISTRAR SALIDA
    // ================================
    public void registrarSalida(Long idContenedor, Long idDeposito, LocalDateTime fechaSalida) {
        try {
            String url = estadiasUrl + "/salida";

            Map<String, Object> body = new HashMap<>();
            body.put("idContenedor", idContenedor);
            body.put("idDeposito", idDeposito);
            body.put("fechaSalida", fechaSalida.toString());

            restTemplate.put(url, body);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error registrando salida de estadía: " + e.getMessage());
        }
    }

    // ================================
    // LISTAR ESTADÍAS POR CONTENEDOR
    // ================================
    public List<Map<String, Object>> obtenerEstadiasPorContenedor(Long idContenedor) {
        try {
            String url = estadiasUrl + "/contenedor/" + idContenedor;
            return restTemplate.getForObject(url, List.class);
        } catch (Exception e) {
            throw new RuntimeException("❌ Error obteniendo estadías del contenedor: " + e.getMessage());
        }
    }
}
