package ar.edu.utnfc.backend.repository;

import ar.edu.utnfc.backend.model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
}