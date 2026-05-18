package ar.edu.utnfc.backend.atencion.service;

import ar.edu.utnfc.backend.atencion.dto.SolicitudRequestDTO;
import ar.edu.utnfc.backend.atencion.dto.CamionDTO;
import ar.edu.utnfc.backend.atencion.model.Cliente;
import ar.edu.utnfc.backend.atencion.model.Contenedor;
import ar.edu.utnfc.backend.atencion.model.Estado;
import ar.edu.utnfc.backend.atencion.model.Solicitud;
import ar.edu.utnfc.backend.atencion.repository.ClienteRepository;
import ar.edu.utnfc.backend.atencion.repository.ContenedorRepository;
import ar.edu.utnfc.backend.atencion.repository.EstadoRepository;
import ar.edu.utnfc.backend.atencion.repository.SolicitudRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class SolicitudService {

    private final SolicitudRepository repo;
    private final EstadoRepository estadoRepo;
    private final ClienteRepository clienteRepo;
    private final ContenedorRepository contenedorRepo;

    private final TransporteClient transporteClient;
    private final RutaClient rutaClient;
    private final TarifaClient tarifaClient;
    private final DepositoClient depositoClient;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public SolicitudService(
            SolicitudRepository repo,
            EstadoRepository estadoRepo,
            ClienteRepository clienteRepo,
            ContenedorRepository contenedorRepo,
            TransporteClient transporteClient,
            RutaClient rutaClient,
            TarifaClient tarifaClient,
            DepositoClient depositoClient
    ) {
        this.repo = repo;
        this.estadoRepo = estadoRepo;
        this.clienteRepo = clienteRepo;
        this.contenedorRepo = contenedorRepo;
        this.transporteClient = transporteClient;
        this.rutaClient = rutaClient;
        this.tarifaClient = tarifaClient;
        this.depositoClient = depositoClient;
    }

    // ==========================================================
    // Listar
    // ==========================================================
    public List<Solicitud> listar() {
        return repo.findAll();
    }

    // ==========================================================
    // Crear Solicitud
    // ==========================================================
    public Solicitud crearDesdeDTO(SolicitudRequestDTO dto) {

        Cliente cliente;
        if (dto.getCliente().getId() != null) {
            cliente = clienteRepo.findById(dto.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        } else {
            cliente = clienteRepo.save(dto.getCliente());
        }

        Contenedor cont = new Contenedor();
        cont.setPeso(dto.getContenedor().getPeso());
        cont.setVolumen(dto.getContenedor().getVolumen());
        cont.setCliente(cliente);

        Estado estadoCont = estadoRepo.findByNombre("No retirado");
        if (estadoCont == null)
            throw new RuntimeException("No existe estado 'No retirado'");

        cont.setEstado(estadoCont);
        contenedorRepo.save(cont);

        Solicitud s = new Solicitud();
        s.setLatitudOrigen(dto.getLatitudOrigen().toString());
        s.setLongitudOrigen(dto.getLongitudOrigen().toString());
        s.setLatitudDestino(dto.getLatitudDestino().toString());
        s.setLongitudDestino(dto.getLongitudDestino().toString());
        s.setCliente(cliente);
        s.setContenedor(cont);

        Estado estadoSol = estadoRepo.findByNombre("Borrador");
        if (estadoSol == null)
            throw new RuntimeException("No existe estado 'Borrador'");
        s.setEstado(estadoSol);

        Integer tiempoEstimado = null;
        try {
            tiempoEstimado = rutaClient.obtenerTiempoEstimado(
                    dto.getLatitudOrigen().toString(),
                    dto.getLongitudOrigen().toString(),
                    dto.getLatitudDestino().toString(),
                    dto.getLongitudDestino().toString()
            );

            if (tiempoEstimado != null)
                s.setTiempoEstimado(tiempoEstimado);

        } catch (Exception e) {
            System.err.println("⚠ Error servicio Rutas: " + e.getMessage());
        }

        try {
            if (tiempoEstimado != null) {
                Long tarifaId = 1L;

                Double costo = tarifaClient.obtenerCostoEstimado(
                        tiempoEstimado.doubleValue(),
                        tarifaId
                );

                if (costo != null)
                    s.setCostoEstimado(BigDecimal.valueOf(costo));
            }

        } catch (Exception e) {
            System.err.println("⚠ Error servicio Tarifas: " + e.getMessage());
        }

        return repo.save(s);
    }

    // ==========================================================
    // Buscar solicitud
    // ==========================================================
    public Solicitud buscar(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ==========================================================
    // Eliminar
    // ==========================================================
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ==========================================================
    // Cambio de estado sencillo
    // ==========================================================
    public Solicitud cambiarEstado(Long idSolicitud, Long idEstado) {
        Solicitud sol = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Estado nuevoEstado = estadoRepo.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        sol.setEstado(nuevoEstado);
        return repo.save(sol);
    }

    // ==========================================================
    // ASIGNAR RUTA (RF4) — ***CORREGIDO***
    // ==========================================================
    public Solicitud asignarRuta(Long idSolicitud, Long idRuta) {

        // ❌ BLOQUEAMOS rutas viejas por seguridad (1,2,3,4,5)
        if (idRuta < 6) {
            throw new RuntimeException("❌ Ruta obsoleta. Debe usarse la ruta REAL creada desde tentativas.");
        }

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("❌ Solicitud no encontrada"));

        // ✔ Asignamos la ruta real (la correcta)
        solicitud.setIdRuta(idRuta);

        // -----------------------------------------------------------------------
        // Verificación del depósito para extraer tarifa vigente
        // -----------------------------------------------------------------------
        Map<String, Object> ruta = rutaClient.obtenerRutaPorId(idRuta);

        if (ruta == null)
            throw new RuntimeException("❌ No se pudo obtener la ruta desde MS Rutas");

        List<Map<String, Object>> tramos =
                (List<Map<String, Object>>) ruta.get("tramos");

        if (tramos == null || tramos.isEmpty())
            throw new RuntimeException("❌ La ruta no tiene tramos asociados");

        Map<String, Object> primerTramo = tramos.get(0);

        Long idDeposito = null;

        if (primerTramo.get("idDepositoOrigen") != null)
            idDeposito = Long.valueOf(primerTramo.get("idDepositoOrigen").toString());

        if (idDeposito == null && primerTramo.get("idDepositoDestino") != null)
            idDeposito = Long.valueOf(primerTramo.get("idDepositoDestino").toString());

        if (idDeposito == null)
            throw new RuntimeException("❌ No se pudo determinar depósito asociado a la ruta");

        Map<String, Object> deposito = depositoClient.obtenerDepositoPorId(idDeposito);

        if (deposito == null || !deposito.containsKey("idTarifaVigente"))
            throw new RuntimeException("❌ El depósito no indica tarifa vigente");

        Long idTarifa = Long.valueOf(deposito.get("idTarifaVigente").toString());

        solicitud.setIdTarifa(idTarifa);

        Estado programada = estadoRepo.findByNombre("Programada");
        if (programada == null)
            throw new RuntimeException("❌ No existe estado 'Programada'");

        solicitud.setEstado(programada);

        return repo.save(solicitud);
    }

    // ==========================================================
    // INICIO TRANSITO
    // ==========================================================
    public Solicitud iniciarTransito(Long idSolicitud) {
        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Estado enTransito = estadoRepo.findByNombre("En tránsito");
        if (enTransito == null)
            throw new RuntimeException("No existe estado 'En tránsito'");

        solicitud.setEstado(enTransito);
        return repo.save(solicitud);
    }

    // ==========================================================
    // FINALIZAR TRANSITO
    // ==========================================================
    public Solicitud finalizar(Long idSolicitud) {
        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Estado entregada = estadoRepo.findByNombre("Entregado");
        if (entregada == null)
            throw new RuntimeException("No existe estado 'Entregado'");

        solicitud.setEstado(entregada);
        return repo.save(solicitud);
    }

    // ==========================================================
    // ASIGNAR CAMIÓN (RF6)
    // ==========================================================
    public Solicitud asignarCamion(Long idSolicitud, Long idTramo, Long idCamion) {

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getIdRuta() == null)
            throw new RuntimeException("La solicitud no tiene ruta asignada");

        Contenedor cont = solicitud.getContenedor();
        if (cont == null)
            throw new RuntimeException("La solicitud no tiene contenedor asociado");

        Double peso = cont.getPeso();
        Double volumen = cont.getVolumen();

        CamionDTO camion = transporteClient.obtenerCamionPorId(idCamion);

        if (camion == null)
            throw new RuntimeException("Camión no encontrado");

        if (peso > camion.getCapacidadPeso())
            throw new RuntimeException("El camión no soporta el peso del contenedor");

        if (volumen > camion.getCapacidadVolumen())
            throw new RuntimeException("El camión no soporta el volumen del contenedor");

        rutaClient.asignarCamion(
                solicitud.getIdRuta(),
                idTramo,
                idCamion
        );

        return solicitud;
    }

    // ==========================================================
    // NOTIFICACIONES: ENTRADA / SALIDA
    // ==========================================================
    private void registrarEntradaEnDeposito(Solicitud solicitud, Map<String, Object> tramo) {

        if (tramo.get("idDepositoDestino") == null) return;

        Long idDepositoDestino = Long.valueOf(tramo.get("idDepositoDestino").toString());
        Long idContenedor = solicitud.getContenedor().getId();

        String fechaFinStr = (String) tramo.get("fechaHoraFin");
        LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, FORMATTER);

        depositoClient.registrarEntrada(idContenedor, idDepositoDestino, fechaFin);
    }

    private void registrarSalidaDeDeposito(Solicitud solicitud, Map<String, Object> tramo) {

        if (tramo.get("idDepositoOrigen") == null) return;

        Long idDepositoOrigen = Long.valueOf(tramo.get("idDepositoOrigen").toString());
        Long idContenedor = solicitud.getContenedor().getId();

        String fechaInicioStr = (String) tramo.get("fechaHoraInicio");
        LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, FORMATTER);

        depositoClient.registrarSalida(idContenedor, idDepositoOrigen, fechaInicio);
    }

    public void notificarInicioTramo(Long idSolicitud, Long idTramo) {

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Map<String, Object> tramo = rutaClient.obtenerTramoPorId(idTramo);

        registrarSalidaDeDeposito(solicitud, tramo);
    }

    public void notificarFinTramo(Long idSolicitud, Long idTramo) {

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Map<String, Object> tramo = rutaClient.obtenerTramoPorId(idTramo);

        registrarEntradaEnDeposito(solicitud, tramo);
    }

    // ==========================================================
    // OBTENER ESTADIAS REALES
    // ==========================================================
    public List<Map<String, Object>> obtenerEstadiasReales(Long idSolicitud) {

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Long idContenedor = solicitud.getContenedor().getId();

        return depositoClient.obtenerEstadiasPorContenedor(idContenedor);
    }

    // ==========================================================
    // COSTO ESTADIAS REALES
    // ==========================================================
    public Double calcularCostoEstadiasReales(Long idSolicitud) {

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        Long idContenedor = solicitud.getContenedor().getId();

        List<Map<String, Object>> estadias =
                depositoClient.obtenerEstadiasPorContenedor(idContenedor);

        double total = 0.0;

        for (Map<String, Object> e : estadias) {

            Long idDeposito = Long.valueOf(e.get("idDeposito").toString());
            Long dias = Long.valueOf(e.get("dias").toString());

            Map<String, Object> deposito = depositoClient.obtenerDepositoPorId(idDeposito);

            if (deposito == null || !deposito.containsKey("costoEstadiaDiario"))
                throw new RuntimeException("El depósito no contiene costoEstadiaDiario");

            double costoDiario = Double.parseDouble(deposito.get("costoEstadiaDiario").toString());

            total += dias * costoDiario;
        }

        return total;
    }

    // ==========================================================
    // CALCULAR COSTO FINAL (RF8)
    // ==========================================================
    public Solicitud calcularCostoFinal(Long idSolicitud) {

        Solicitud solicitud = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getIdRuta() == null)
            throw new RuntimeException("La solicitud no tiene ruta asignada");

        Map<String, Object> ruta = rutaClient.obtenerRutaPorId(solicitud.getIdRuta());

        List<Map<String, Object>> tramos =
                (List<Map<String, Object>>) ruta.get("tramos");

        if (tramos == null || tramos.isEmpty())
            throw new RuntimeException("La ruta no tiene tramos");

        double costoRecorrido = 0.0;
        double costoGestion = 0.0;
        int tiempoRealTotal = 0;

        Long idTarifa = solicitud.getIdTarifa();
        Map<String, Object> tarifa = tarifaClient.obtenerTarifaPorId(idTarifa);

        double costoGestionTramo =
                Double.parseDouble(tarifa.get("costoGestionTramo").toString());

        double costoKmBase =
                Double.parseDouble(tarifa.get("costoKmBase").toString());

        for (Map<String, Object> tramo : tramos) {

            if (tramo.get("costoReal") != null) {
                costoRecorrido += Double.parseDouble(tramo.get("costoReal").toString());
            }

            String inicio = (String) tramo.get("fechaHoraInicio");
            String fin = (String) tramo.get("fechaHoraFin");

            if (inicio != null && fin != null) {

                LocalDateTime i = LocalDateTime.parse(inicio, FORMATTER);
                LocalDateTime f = LocalDateTime.parse(fin, FORMATTER);

                tiempoRealTotal += java.time.Duration.between(i, f).toMinutes();
            }

            costoGestion += costoGestionTramo;
        }

        double costoPorTiempoReal = (tiempoRealTotal / 60.0) * costoKmBase;

        double costoEstadias = calcularCostoEstadiasReales(idSolicitud);

        double costoFinal =
                costoRecorrido +
                costoGestion +
                costoPorTiempoReal +
                costoEstadias;

        solicitud.setCostoFinal(BigDecimal.valueOf(costoFinal));
        solicitud.setTiempoReal(tiempoRealTotal);

        return repo.save(solicitud);
    }
}
