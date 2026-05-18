package ar.edu.utnfc.backend.atencion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double peso;
    private Double volumen;

    // 🔹 Evita recursión: oculta cliente y contenedor dentro de la solicitud
    @OneToOne(mappedBy = "contenedor")
    @JsonIgnoreProperties({"contenedor", "cliente"})
    private Solicitud solicitud;

    // 🔹 Muestra cliente resumido, sin recursión infinita
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"contenedores", "solicitudes", "barrio"})
    private Cliente cliente;

    // 🔹 Agregado del RNF2 — Estado del contenedor
    @ManyToOne
    @JoinColumn(name = "estado_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Estado estado;
}
