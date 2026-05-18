package ar.edu.utnfc.backend.atencion.repository;

import ar.edu.utnfc.backend.atencion.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    Optional<Solicitud> findByContenedor_Id(Long idContenedor);

}
