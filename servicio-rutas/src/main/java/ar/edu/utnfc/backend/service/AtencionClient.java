package ar.edu.utnfc.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AtencionClient {

    @Value("${servicios.atencion.url}")
    private String atencionUrl;

    private final RestTemplate rest = new RestTemplate();

    // ===============================================
    // NOTIFICAR INICIO DE TRAMO
    // ===============================================
    public void notificarInicioTramo(Long idSolicitud, Long idTramo) {

        String url = atencionUrl
                + "/api/solicitudes/notificar-inicio-tramo"
                + "?idSolicitud=" + idSolicitud
                + "&idTramo=" + idTramo;

        rest.put(url, null);
    }

    // ===============================================
    // NOTIFICAR FIN DE TRAMO
    // ===============================================
    public void notificarFinTramo(Long idSolicitud, Long idTramo) {

        String url = atencionUrl
                + "/api/solicitudes/notificar-fin-tramo"
                + "?idSolicitud=" + idSolicitud
                + "&idTramo=" + idTramo;

        rest.put(url, null);
    }
}
