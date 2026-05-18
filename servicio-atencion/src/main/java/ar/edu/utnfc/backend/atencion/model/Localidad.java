package ar.edu.utnfc.backend.atencion.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Entity
@Getter @Setter
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "localidad")
    private List<Barrio> barrios;
}