package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.Util;
import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ButacaReservaDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.VentaDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;
import com.codoacodo.flysky.demo.exception.EntityNotFoundException;
import com.codoacodo.flysky.demo.exception.UnAuthorizedException;
import com.codoacodo.flysky.demo.model.entity.Butaca;
import com.codoacodo.flysky.demo.model.entity.Reserva;
import com.codoacodo.flysky.demo.model.entity.Usuario;
import com.codoacodo.flysky.demo.model.entity.Vuelo;
import com.codoacodo.flysky.demo.model.enums.TipoUsuario;
import com.codoacodo.flysky.demo.repository.IButacaRepository;
import com.codoacodo.flysky.demo.repository.IReservaRepository;
import com.codoacodo.flysky.demo.repository.IUsuarioRepository;
import com.codoacodo.flysky.demo.repository.IVueloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VueloServiceImpl implements IVueloService {

    IVueloRepository vueloRepository;
    IUsuarioRepository usuarioRepository;
    IButacaRepository butacaRepository;
    IReservaRepository reservaRepository;

    public VueloServiceImpl(IVueloRepository vueloRepository, IUsuarioRepository usuarioRepository,
                            IButacaRepository butacaRepository, IReservaRepository reservaRepository) {
        this.vueloRepository = vueloRepository;
        this.usuarioRepository = usuarioRepository;
        this.butacaRepository = butacaRepository;
        this.reservaRepository = reservaRepository;
    }

    ModelMapper mapper = new ModelMapper();

    @Override
    public List<VueloDto> obtenerVuelosDisponibles(String nombreUsuarioTipoCliente) {

        Optional<Usuario> usuario = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

        if (usuario.isEmpty()) {
            throw new NoSuchElementException("Usuario no registrado. Registrese como CLIENTE para poder visualizar " +
                    "vuelos disponibles.");
        }

        if (!usuario.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {
            throw new UnAuthorizedException("Usuario registrado como " + usuario.get().getTipoUsuario() + ". Registrese " +
                    "como CLIENTE para poder visualizar vuelos disponibles.");
        }

        List<Vuelo> vuelos = vueloRepository.findAll();

        //si tabla vuelo no tiene cargada información , la base de datos va a retornar lista de vuelos vacía sin lanzar una excepción.
        if (vuelos.isEmpty()) {
            throw new EntityNotFoundException("La lista de vuelos está vacía.");
        }

        List<Vuelo> vuelosDisponibles = new ArrayList<>();

        LocalDateTime fechaHoraActual = LocalDateTime.now();

        for (Vuelo vuelo : vuelos) {
            LocalDateTime fechaHoraVuelo = vuelo.getFechaHoraPartida();

            if (fechaHoraVuelo.isAfter(fechaHoraActual) && disponibilidadButaca(vuelo)) {
                vuelosDisponibles.add(vuelo);
            }
        }

        if (vuelosDisponibles.isEmpty()) {
            throw new EntityNotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
        }

        List<VueloDto> vuelosDto = new ArrayList<>();
        vuelosDisponibles.forEach(vueloDisponible -> vuelosDto.add(mapper.map(vueloDisponible, VueloDto.class)));

        //Otra alternativa
        //List<VueloDto> vuelosDto = vuelosDisponibles.stream().map(vueloDisponible-> mapper.map(vueloDisponible, VueloDto.class)).toList();

        return vuelosDto;
    }

    private boolean disponibilidadButaca(Vuelo vuelo) {
        for (Butaca butaca : vuelo.getButacas()) {
            if (butaca.getDisponible()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public ReservaDto reservarVuelo(String nombreUsuarioTipoCliente, ReservaVueloDto
            reservaVueloDto) {

        //Otra alternativa para tratar un Optional y lanzar una excepción.
        Usuario usuarioCliente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente)
                .orElseThrow(() -> new NoSuchElementException("Usuario no registrado. Registrese como CLIENTE para " +
                        "poder realizar reservas."));

        if (usuarioCliente.getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

            Vuelo vueloReserva = vueloRepository.findByNumeroVuelo(reservaVueloDto.getNumeroVuelo())
                    .orElseThrow(() -> new NoSuchElementException("El vuelo que quiere reservar no existe."));

            if (vueloReserva.getFechaHoraPartida().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("El vuelo que intenta reservar ya caducó.");
            }

            List<Butaca> butacasDelVuelo = vueloReserva.getButacas();

            if (butacasDelVuelo.isEmpty()) {
                throw new EntityNotFoundException("El vuelo que quiere reservar no tiene asignadas butacas.");
            }

            List<Butaca> butacasReserva = new ArrayList<>();

            for (ButacaReservaDto butacaReservaDto : reservaVueloDto.getButacas()) {
                Butaca butacaExistente = butacasDelVuelo.stream()
                        .filter(butacaDelVuelo -> butacaDelVuelo.getPosicion().equals(butacaReservaDto.getPosicion()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("La posición " + butacaReservaDto.getPosicion() +
                                " de la butaca que intenta reservar no pertenece al vuelo."));

                if (!butacaExistente.getDisponible()) {
                    throw new EntityNotFoundException("La posición " + butacaReservaDto.getPosicion() + " de la butaca" +
                            " que intenta reservar ya se encuentra ocupada.");
                }

                butacaExistente.setDisponible(Boolean.FALSE);
                butacaExistente.setNombrePasajero(butacaReservaDto.getNombrePasajero());

                butacasReserva.add(butacaExistente);
            }
            //solamente me setea en falso la disponibilidad en la entidad Butaca, además de setear la reserva en la entidad Reserva
            Reserva reservaEntity = reservaRepository
                    .save(crearReservaEntityPersistencia(reservaVueloDto, vueloReserva, usuarioCliente, butacasReserva));

            //modifico el null de reserva_id en la entidad Butaca por el id de la reserva
            butacasReserva.forEach(butacaReserva -> butacaReserva.setReserva(reservaEntity));
            butacaRepository.saveAll(butacasReserva);

            return mapper.map(reservaEntity, ReservaDto.class);

        }
        throw new UnAuthorizedException("Usuario registrado como " + usuarioCliente.getTipoUsuario() + ". Registrese como " +
                "CLIENTE para realizar una reserva.");

    }

    private Reserva crearReservaEntityPersistencia(ReservaVueloDto reservaVueloDto,
                                                   Vuelo vueloReserva,
                                                   Usuario usuario,
                                                   List<Butaca> butacasReserva) {
        Reserva reservaEntityPersistencia = new Reserva();
        reservaEntityPersistencia.setTipoPago(reservaVueloDto.getTipoPago());
        reservaEntityPersistencia.setMontoPago
                (Util.montoAPagar(reservaVueloDto.getTipoPago(), vueloReserva.getPrecio()) * butacasReserva.size());
        reservaEntityPersistencia.setFechaReserva(LocalDateTime.now());
        reservaEntityPersistencia.setUsuario(usuario);
        reservaEntityPersistencia.setVuelo(vueloReserva);
        //seteamos las butacas para poder realizar el mapper de reservaEntity y nos retorne las butacas reservadas
        reservaEntityPersistencia.setButacas(butacasReserva);

        return reservaEntityPersistencia;
    }

    @Override
    public List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String
            nombreUsuarioTipoCliente) {

        //Si no lo manejamos con un Optional y usuarioAgente no existe, Spring nos lanza un NullPointerException(500 Internal Server Error)
        //Si lo manejamos con un Optional y usuarioAgente no existe, Spring nos lanza un NoSuchElementException(500 Internal Server Error)
        Optional<Usuario> usuarioAgente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAgente);

        if (usuarioAgente.isPresent()) {
            //if (usuarioAgente.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Agente de ventas"))
            if (usuarioAgente.get().getTipoUsuario().equals(TipoUsuario.AGENTE_DE_VENTAS)) {

                Optional<Usuario> usuarioCliente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

                if (usuarioCliente.isPresent()) {
                    if (usuarioCliente.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

                        List<Reserva> reservasEntity = usuarioCliente.get().getReserva();

                        //Otra alternativa pero menos eficiente, ya que estoy llamando a dos repositorios para el mismo
                        // fin, es decir, pido dos veces las reservas.
                        //List<ReservaEntity> reservasEntity = reservaRepository.findByUsuario(usuarioCliente.get());

                        if (reservasEntity.isEmpty()) {
                            throw new EntityNotFoundException("No hay reservas realizadas por el usuario "
                                    + nombreUsuarioTipoCliente + ".");
                        }

                        return reservasEntity.stream()
                                .map(reservaEntity -> mapper.map(reservaEntity, ReservaDto.class))
                                .toList();

                    }
                    throw new EntityNotFoundException("El usuario al que pretende visualizar sus reservas está registrado " +
                            "como " + usuarioAgente.get().getTipoUsuario() + " por lo que no tiene reservas, ya que no " +
                            "las puede hacer.");
                }
                throw new NoSuchElementException("El usuario al que pretende visualizar sus reservas no está registrado.");
            }
            throw new UnAuthorizedException("Usuario registrado como " + usuarioAgente.get().getTipoUsuario() + ". Registrese " +
                    "como Agente de ventas para poder visualizar el listado de reservas.");
        }
        throw new NoSuchElementException("Usuario no registrado. Registrese como Agente de ventas para poder " +
                "visualizar el listado de reservas por cliente.");
    }

    @Override
    public VentaDto obtenerNumeroVentasIngresosDiarios(String nombreUsuarioTipoAdministrador, LocalDate fecha) {

        Optional<Usuario> usuarioAdministrador = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAdministrador);

        if (usuarioAdministrador.isEmpty()) {
            throw new NoSuchElementException("Usuario no registrado. Registrese como ADMINISTRADOR para poder visualizar " +
                    "el número de ventas e ingresos generados diarios.");
        }

        if (!usuarioAdministrador.get().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
            throw new UnAuthorizedException("Usuario registrado como " + usuarioAdministrador.get().getTipoUsuario() +
                    ". Registrese como ADMINISTRADOR para poder visualizar el número de ventas e ingresos generados " +
                    "diarios");
        }

        List<Reserva> reservasEntity = reservaRepository.findAll();

        if (reservasEntity.isEmpty()) {
            throw new EntityNotFoundException("No hay reservas realizadas.");
        }

        List<Reserva> reservasFecha = reservasEntity.stream()
                .filter(reserva -> reserva.getFechaReserva().toLocalDate().equals(fecha))
                .toList();

        if (reservasFecha.isEmpty()) {
            //throw new EntityNotFoundException("No hay reservas realizadas el " + Util.fechaString(fecha) + ".");
            throw new EntityNotFoundException("No hay reservas realizadas el " + fecha.format(DateTimeFormatter
                    .ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"))) + ".");
        }

        VentaDto ventaDto = new VentaDto();
        ventaDto.setCantidadVenta(reservasFecha.size());

        List<Double> montosPago = reservasFecha.stream().map(Reserva::getMontoPago).toList();

        ventaDto.setIngreso(montosPago.stream()
                .reduce(0.0, Double::sum)); //operación de reducción para realizar la suma

        /*Otra alternativa using Lambda expression i ->i
        double ingreso = reservasFecha.stream()
                .mapToDouble(reserva -> reserva.getMontoPago())
                .sum();
        */

        return ventaDto;
    }
/*
    @Override
    public void borrarVuelos(Long id) {
        //Orden de borrado siempre y cuando no se utilice fetch = FetchType.EAGER
        butacaRepository.deleteAll(); //No utilizar fetch = FetchType.EAGER en la entidades relacionadas a Butaca (Reserva y Vuelo)
        //usuarioRepository.deleteAll();//Agregar CascadeType.REMOVE en el atributo butacas en la entidad Reserva para borrar las reservas del usuario.
        //reservaRepository.deleteAll(); //Si pretendemos borrar todas las reservas
        //vueloRepository.deleteAll();

    }
*/
}