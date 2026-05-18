package ar.edu.utnfc.backend.repository;

import ar.edu.utnfc.backend.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByNombreAndAmbito(String nombre, String ambito);
}