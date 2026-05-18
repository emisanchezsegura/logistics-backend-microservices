package ar.edu.utnfc.backend.atencion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CamionDTO {
    private Long idCamion;
    private String dominio;
    private String marca;
    private String modelo;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Double consumoCombustibleKm;
    private Double costoBaseKm;
    private Boolean disponible;
    private Long idTransportista;
}
