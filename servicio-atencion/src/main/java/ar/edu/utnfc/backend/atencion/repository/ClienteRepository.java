package ar.edu.utnfc.backend.atencion.repository;

import ar.edu.utnfc.backend.atencion.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Si querés agregar consultas personalizadas, las ponés acá.
    // Ejemplo: Optional<Cliente> findByEmail(String email);
}