package ar.edu.utnfc.backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;   // ESTIMADO, ASIGNADO, INICIADO, FINALIZADO
    private String ambito;   // TRAMO, SOLICITUD, RUTA, etc.
}
