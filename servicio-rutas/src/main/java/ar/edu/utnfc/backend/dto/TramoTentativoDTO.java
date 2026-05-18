package ar.edu.utnfc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TramoTentativoDTO {

    private Long idDepositoOrigen;   // puede ser null
    private Long idDepositoDestino;  // puede ser null

    private Double distanciaKm;
    private Integer tiempoMin;
    private Double costoTentativo;
}
