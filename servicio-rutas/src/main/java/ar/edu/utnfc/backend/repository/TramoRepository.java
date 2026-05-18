package ar.edu.utnfc.backend.repository;

import ar.edu.utnfc.backend.model.Tramo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TramoRepository extends JpaRepository<Tramo, Long> {

    // 👉 Necesario para que Atención pueda obtener los tramos de la ruta
    List<Tramo> findByRutaIdRuta(Long idRuta);

}
