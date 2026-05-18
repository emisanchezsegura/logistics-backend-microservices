package ar.edu.utnfc.backend.dto;

import lombok.Data;

@Data
public class RutaTentativaRequestDTO {

    private Double latOrigen;
    private Double lonOrigen;

    private Double latDestino;
    private Double lonDestino;
}
