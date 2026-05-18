package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.model.Barrio;
import ar.edu.utnfc.backend.repository.BarrioRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrioService {

    private final BarrioRepository barrioRepository;

    public BarrioService(BarrioRepository barrioRepository) {
        this.barrioRepository = barrioRepository;
    }

    public List<Barrio> obtenerTodos() {
        return barrioRepository.findAll();
    }

    public Optional<Barrio> obtenerPorId(Long id) {
        return barrioRepository.findById(id);
    }

    public List<Barrio> obtenerPorLocalidad(String nombreLocalidad) {
        return barrioRepository.findByLocalidad_Nombre(nombreLocalidad);
    }

    public Barrio guardar(Barrio barrio) {
        return barrioRepository.save(barrio);
    }
}
