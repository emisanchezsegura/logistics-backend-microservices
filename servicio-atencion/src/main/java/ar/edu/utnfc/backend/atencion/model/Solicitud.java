package ar.edu.utnfc.backend.atencion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter @Setter
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud = LocalDate.now();

    // 📍 Coordenadas de origen y destino
    private String latitudOrigen;
    private String longitudOrigen;
    private String latitudDestino;
    private String longitudDestino;

    // 💰 Costos y tiempos
    private BigDecimal costoEstimado;
    private BigDecimal costoFinal;
    private Integer tiempoEstimado;  // en minutos
    private Integer tiempoReal;

    // 👤 Relaciones internas (entidades del mismo microservicio)
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne
    @JoinColumn(name = "contenedor_id")
    private Contenedor contenedor;

    // 🔹 Nueva relación real con la entidad Estado (en vez de Long idEstado)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Estado estado;

    // 🔹 Referencias lógicas a otros microservicios (se mantienen como IDs)
    private Long idRuta;     // FK lógica al microservicio de rutas
    private Long idTarifa;   // FK lógica al microservicio de tarifas
}
