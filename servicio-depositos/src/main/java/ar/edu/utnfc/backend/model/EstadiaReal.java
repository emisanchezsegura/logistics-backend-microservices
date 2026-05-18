package ar.edu.utnfc.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estadias_reales")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EstadiaReal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idContenedor;

    @Column(nullable = false)
    private Long idDeposito;

    @Column(nullable = false)
    private LocalDateTime fechaEntrada;

    @Column
    private LocalDateTime fechaSalida; // null hasta que salga

    @Column
    private Long dias; // cálculo automático cuando se registra salida
}
