package ar.edu.utnfc.backend.atencion.service;

import ar.edu.utnfc.backend.atencion.dto.ContenedorPendienteDTO;
import ar.edu.utnfc.backend.atencion.model.Cliente;
import ar.edu.utnfc.backend.atencion.model.Contenedor;
import ar.edu.utnfc.backend.atencion.model.Estado;
import ar.edu.utnfc.backend.atencion.model.Solicitud;
import ar.edu.utnfc.backend.atencion.repository.ClienteRepository;
import ar.edu.utnfc.backend.atencion.repository.ContenedorRepository;
import ar.edu.utnfc.backend.atencion.repository.EstadoRepository;
import ar.edu.utnfc.backend.atencion.repository.SolicitudRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContenedorService {

    private final ContenedorRepository repo;
    private final ClienteRepository clienteRepo;
    private final EstadoRepository estadoRepo;
    private final SolicitudRepository solicitudRepo;

    public ContenedorService(
            ContenedorRepository repo,
            ClienteRepository clienteRepo,
            EstadoRepository estadoRepo,
            SolicitudRepository solicitudRepo
    ) {
        this.repo = repo;
        this.clienteRepo = clienteRepo;
        this.estadoRepo = estadoRepo;
        this.solicitudRepo = solicitudRepo;
    }

    // ==========================================================
    // Listar todos
    // ==========================================================
    public List<Contenedor> listar() {
        return repo.findAll();
    }

    // ==========================================================
    // Crear contenedor genérico
    // ==========================================================
    public Contenedor crear(Contenedor contenedor) {

        Estado estadoInicial = estadoRepo.findByNombreAndAmbito("No retirado", "CONTENEDOR")
                .orElseThrow(() -> new RuntimeException(
                        "Estado inicial 'No retirado' / CONTENEDOR no encontrado"
                ));

        contenedor.setEstado(estadoInicial);

        return repo.save(contenedor);
    }

    // ==========================================================
    // Buscar por ID
    // ==========================================================
    public Contenedor buscar(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ==========================================================
    // RF1 — Contenedores por cliente
    // ==========================================================
    public List<Contenedor> listarPorCliente(Long idCliente) {

        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado con ID " + idCliente)
                );

        return cliente.getContenedores();
    }

    // ==========================================================
    // RF1 — Crear contenedor asignado a cliente
    // ==========================================================
    public Contenedor crearParaCliente(Long idCliente, Contenedor contenedor) {

        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado con ID " + idCliente)
                );

        contenedor.setCliente(cliente);

        Estado estadoInicial = estadoRepo.findByNombreAndAmbito("No retirado", "CONTENEDOR")
                .orElseThrow(() -> new RuntimeException(
                        "Estado inicial 'No retirado' / CONTENEDOR no encontrado"));

        contenedor.setEstado(estadoInicial);

        return repo.save(contenedor);
    }

    // ==========================================================
    // Eliminar
    // ==========================================================
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ==========================================================
    // RNF2 — Cambiar estado del contenedor
    // ==========================================================
    public Contenedor actualizarEstado(Long idContenedor, Long idEstado) {

        Contenedor contenedor = repo.findById(idContenedor)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));

        Estado estado = estadoRepo.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        contenedor.setEstado(estado);

        return repo.save(contenedor);
    }

    // ==========================================================
    // RNF2 — Obtener estado actual
    // ==========================================================
    public Estado obtenerEstado(Long idContenedor) {

        Contenedor contenedor = repo.findById(idContenedor)
                .orElseThrow(() -> new RuntimeException("Contenedor no encontrado"));

        return contenedor.getEstado();
    }

    // ==========================================================
    // ⭐⭐⭐ RF5 — Contenedores pendientes de entrega + Ubicación ⭐⭐⭐
    // ==========================================================
    public List<ContenedorPendienteDTO> listarPendientes() {

        List<ContenedorPendienteDTO> resultado = new ArrayList<>();

        List<Contenedor> contenedores = repo.findAll();

        for (Contenedor c : contenedores) {

            // 1️⃣ Solicitud asociada
            Solicitud solicitud = solicitudRepo.findByContenedor_Id(c.getId())
                    .orElse(null);

            if (solicitud == null)
                continue;

            // 2️⃣ Ignorar entregados
            if (c.getEstado().getNombre().equalsIgnoreCase("Entregado"))
                continue;

            // 3️⃣ Ubicación lógica según estado del contenedor
            String ubicacion = switch (c.getEstado().getNombre()) {
                case "No retirado" -> "En origen";
                case "Retirado" -> "Retirado del origen";
                case "En viaje" -> "En tránsito";
                case "En depósito" -> "En depósito temporal";
                default -> "Ubicación desconocida";
            };

            // 4️⃣ Crear DTO
            resultado.add(new ContenedorPendienteDTO(
                    c.getId(),
                    solicitud.getCliente().getNombre() + " " + solicitud.getCliente().getApellido(),
                    c.getEstado().getNombre(),
                    solicitud.getIdRuta(),
                    null,               // tramo actual lo agregaremos en RF6–RF7
                    ubicacion
            ));
        }

        return resultado;
    }

}
