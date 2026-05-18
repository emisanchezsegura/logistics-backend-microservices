package ar.edu.utnfc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RutaTentativaDTO {

    private Double distanciaTotalKm;
    private Integer tiempoTotalMin;
    private Double costoTotal;

    private List<TramoTentativoDTO> tramos;
}
