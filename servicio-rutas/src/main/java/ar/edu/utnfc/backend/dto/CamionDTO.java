package ar.edu.utnfc.backend.dto;

import lombok.Data;

@Data
public class CamionDTO {
    private Long idCamion;
    private String dominio;
    private String marca;
    private String modelo;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Boolean disponible;
    private Double consumoCombustibleKm;
}
