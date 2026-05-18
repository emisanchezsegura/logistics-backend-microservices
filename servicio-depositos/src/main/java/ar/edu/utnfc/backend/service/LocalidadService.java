package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.model.Localidad;
import ar.edu.utnfc.backend.repository.LocalidadRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadService {

    private final LocalidadRepository localidadRepository;

    public LocalidadService(LocalidadRepository localidadRepository) {
        this.localidadRepository = localidadRepository;
    }

    public List<Localidad> obtenerTodas() {
        return localidadRepository.findAll();
    }

    public Optional<Localidad> obtenerPorId(Long id) {
        return localidadRepository.findById(id);
    }

    public Localidad guardar(Localidad localidad) {
        return localidadRepository.save(localidad);
    }
}
