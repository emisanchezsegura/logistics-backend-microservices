package ar.edu.utnfc.backend.repository;

import ar.edu.utnfc.backend.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CamionRepository extends JpaRepository<Camion, Long> {

    // ✅ Busca camiones disponibles y con capacidad suficiente
    List<Camion> findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(
            Double peso, Double volumen
    );
    Optional<Camion> findById(Long id);
}
