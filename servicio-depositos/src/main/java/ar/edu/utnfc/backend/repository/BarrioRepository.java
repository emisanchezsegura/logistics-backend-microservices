package ar.edu.utnfc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.utnfc.backend.model.Barrio;

import java.util.List;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {

    // Buscar barrios por localidad
    List<Barrio> findByLocalidad_Nombre(String nombreLocalidad);

    // Buscar barrio por nombre
    Barrio findByNombre(String nombre);
}
