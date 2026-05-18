package ar.edu.utnfc.backend.tarifas.client.dto;

import lombok.Data;

@Data
public class DepositoDTO {
    private Long id;
    private String nombre;
    private Double costoEstadiaDiario;
}

