package ar.edu.utnfc.backend.atencion.repository;

import ar.edu.utnfc.backend.atencion.model.Estado;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Estado findByNombre(String nombre);

    Optional<Estado> findByNombreAndAmbito(String nombre, String ambito);
}
