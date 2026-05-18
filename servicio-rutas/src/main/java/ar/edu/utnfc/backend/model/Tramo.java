package ar.edu.utnfc.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tramo")
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramo")
    private Long idTramo;

    @Column(name = "costo_aproximado")
    private Double costoAproximado;

    @Column(name = "costo_real")
    private Double costoReal;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruta")
    @JsonBackReference
    private Ruta ruta;

    @Column(name = "id_camion")
    private Long idCamion;

    @Column(name = "id_tipo_tramo")
    private Long idTipoTramo;

    // 🔹 Relación real con Estado (del archivo 2)
    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;

    @Column(name = "id_deposito_origen")
    private Long idDepositoOrigen;

    @Column(name = "id_deposito_destino")
    private Long idDepositoDestino;
}
