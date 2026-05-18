package ar.edu.utnfc.backend.atencion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContenedorPendienteDTO {

    private Long idContenedor;
    private String cliente;
    private String estadoContenedor;   // ESTADO DEL CONTENEDOR (No retirado / En viaje / etc.)
    private Long idRuta;
    private Long idTramoActual;        // Puede ser null
    private String ubicacionActual;    // Texto obtenido del MS Rutas
}
