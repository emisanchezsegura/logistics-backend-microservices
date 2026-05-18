package ar.edu.utnfc.backend.atencion.repository;


import ar.edu.utnfc.backend.atencion.model.Barrio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {
    // Si querés agregar consultas personalizadas, las ponés acá.
    // Ejemplo: Optional<Cliente> findByEmail(String email);
}