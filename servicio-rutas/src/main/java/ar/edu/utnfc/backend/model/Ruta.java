package ar.edu.utnfc.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ruta")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long idRuta;

    @Column(name = "cantidad_tramos")
    private Integer cantidadTramos;

    @Column(name = "cantidad_depositos")
    private Integer cantidadDepositos;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Tramo> tramos;
}
