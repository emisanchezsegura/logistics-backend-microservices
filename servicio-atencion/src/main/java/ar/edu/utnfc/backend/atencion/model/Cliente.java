package ar.edu.utnfc.backend.atencion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter @Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    private String telefono;

    @Email
    private String email;

    // 🔹 Relación con Contenedor — ignora cliente dentro del contenedor
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"cliente", "solicitud"})
    private List<Contenedor> contenedores;

    // 🔹 Relación con Solicitud — ignora cliente dentro de solicitud
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"cliente", "contenedor"})
    private List<Solicitud> solicitudes;

    @OneToOne
    @JoinColumn(name = "barrio_id")
    private Barrio barrio;
}
