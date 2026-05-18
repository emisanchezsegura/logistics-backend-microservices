package ar.edu.utnfc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.utnfc.backend.model.Deposito;

import java.util.List;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {


    // Buscar depósitos por nombre de barrio
    List<Deposito> findByBarrio_Nombre(String nombreBarrio);

    // Buscar depósitos por localidad
    List<Deposito> findByBarrio_Localidad_Nombre(String nombreLocalidad);
}
