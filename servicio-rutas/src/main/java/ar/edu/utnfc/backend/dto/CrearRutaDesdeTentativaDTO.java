package ar.edu.utnfc.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearRutaDesdeTentativaDTO {

    // 👉 ID de la solicitud que estamos trabajando (por si querés guardarlo luego)
    private Long idSolicitud;

    // 👉 Tramos tentativos convertidos a tramos reales
    private List<TramoTentativoDTO> tramos;

}
