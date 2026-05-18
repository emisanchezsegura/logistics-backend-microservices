package ar.edu.utnfc.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "transportista")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private Boolean activo = true;

    @OneToMany(mappedBy = "transportista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Camion> camiones;
}
