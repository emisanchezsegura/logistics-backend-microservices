package ar.edu.utnfc.backend.service;

import ar.edu.utnfc.backend.model.Camion;
import ar.edu.utnfc.backend.repository.CamionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CamionService {

    private final CamionRepository repo;

    public CamionService(CamionRepository repo) {
        this.repo = repo;
    }

    public List<Camion> listar() {
        return repo.findAll();
    }

    public Camion guardar(Camion camion) {
        return repo.save(camion);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ✅ Nuevo método: filtra camiones aptos
    public List<Camion> obtenerDisponibles(Double peso, Double volumen) {
        return repo.findByDisponibleTrueAndCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(peso, volumen);
    }

    // 🔹 Método adicional del archivo 2 (preservado)
    public Optional<Camion> obtenerPorId(Long id) {
        return repo.findById(id);
    }
}
