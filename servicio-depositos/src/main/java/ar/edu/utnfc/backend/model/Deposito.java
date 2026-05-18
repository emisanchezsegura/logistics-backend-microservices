package ar.edu.utnfc.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "depositos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDeposito;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column
    private Double latitud;

    @Column
    private Double longitud;

    @Column(nullable = false)
    private Double costoEstadiaDiario;

    @Column(name = "id_tarifa_vigente")
    private Long idTarifaVigente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_barrio", nullable = false)
    private Barrio barrio;
}
