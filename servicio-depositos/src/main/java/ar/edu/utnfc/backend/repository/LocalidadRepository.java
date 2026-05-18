package ar.edu.utnfc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.utnfc.backend.model.Localidad;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    // Buscar localidad por nombre
    Localidad findByNombre(String nombre);
}
