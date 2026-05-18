package ar.edu.utnfc.backend.atencion.dto;

import lombok.Data;
import ar.edu.utnfc.backend.atencion.model.Cliente;
import ar.edu.utnfc.backend.atencion.model.Contenedor;

@Data
public class SolicitudRequestDTO {

    private Double latitudOrigen;
    private Double longitudOrigen;
    private Double latitudDestino;
    private Double longitudDestino;

    private Cliente cliente;
    private Contenedor contenedor;
}
