package ar.edu.utnfc.backend.dto;

import lombok.Data;

@Data
public class DepositoDTO {

    private Long idDeposito;

    private String nombre;

    private String direccion;

    private Double latitud;

    private Double longitud;

    private Double costoEstadiaDiario;

    private Long idTarifaVigente;  // ✔ LO AGREGAMOS

    private Long idBarrio;         // ✔ LO AGREGAMOS
}
