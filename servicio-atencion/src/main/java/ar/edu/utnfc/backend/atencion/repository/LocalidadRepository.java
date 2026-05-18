package ar.edu.utnfc.backend.atencion.repository;

import ar.edu.utnfc.backend.atencion.model.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    // Si querés agregar consultas personalizadas, las ponés acá.
    // Ejemplo: Optional<Cliente> findByEmail(String email);

    
}