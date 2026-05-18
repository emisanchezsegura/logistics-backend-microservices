package ar.edu.utnfc.backend.atencion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;        // Ej: "Borrador", "Programada", etc.
    private String descripcion;   // Descripción del estado

    @Column(nullable = false)
    private String ambito;        // "SOLICITUD", "TRAMO", "CONTENEDOR"
}
