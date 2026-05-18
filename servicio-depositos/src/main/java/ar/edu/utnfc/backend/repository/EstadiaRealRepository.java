package ar.edu.utnfc.backend.repository;

import ar.edu.utnfc.backend.model.EstadiaReal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadiaRealRepository extends JpaRepository<EstadiaReal, Long> {

    List<EstadiaReal> findByIdContenedor(Long idContenedor);

    // Para registrar salida: buscamos estadía del contenedor en ese depósito sin cerrar
    EstadiaReal findFirstByIdContenedorAndIdDepositoAndFechaSalidaIsNull(
            Long idContenedor,
            Long idDeposito
    );
}
