package ar.edu.utnfc.backend.tarifas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tarifa")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long idTarifa;

    private String descripcion;

    @Column(name = "costo_km_base")
    private Double costoKmBase;

    @Column(name = "valor_litro_combustible")
    private Double valorLitroCombustible;

    @Column(name = "costo_gestion_tramo")
    private Double costoGestionTramo;

    @Column(name = "fecha_vigencia_desde")
    private LocalDate fechaVigenciaDesde;

    @Column(name = "fecha_vigencia_hasta")
    private LocalDate fechaVigenciaHasta;
}
