package ar.edu.utnfc.backend.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "camion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dominio;
    private String marca;
    private String modelo;

    @Column(name = "capacidad_peso")
    private Double capacidadPeso;
    @Column(name = "capacidad_volumen")
    private Double capacidadVolumen;

    private double consumo_combustible_km;
    private Double costo_base_km;
    private Boolean disponible = true;

    @ManyToOne
    @JoinColumn(name = "transportista_id")
    private Transportista transportista;
}
