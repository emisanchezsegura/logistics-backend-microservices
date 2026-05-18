package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.model.Deposito;
import ar.edu.utnfc.backend.repository.DepositoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoService(DepositoRepository depositoRepository) {
        this.depositoRepository = depositoRepository;
    }

    public List<Deposito> obtenerTodos() {
        return depositoRepository.findAll();
    }

    public Optional<Deposito> obtenerPorId(Long id) {
        return depositoRepository.findById(id);
    }

    public Deposito guardar(Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    public void eliminar(Long id) {
        depositoRepository.deleteById(id);
    }


    public List<Deposito> obtenerPorBarrio(String nombreBarrio) {
        return depositoRepository.findByBarrio_Nombre(nombreBarrio);
    }

    public List<Deposito> obtenerPorLocalidad(String nombreLocalidad) {
        return depositoRepository.findByBarrio_Localidad_Nombre(nombreLocalidad);
    }
}
