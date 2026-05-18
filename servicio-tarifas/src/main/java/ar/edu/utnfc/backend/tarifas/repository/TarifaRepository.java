package ar.edu.utnfc.backend.tarifas.repository;

import ar.edu.utnfc.backend.tarifas.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
}
