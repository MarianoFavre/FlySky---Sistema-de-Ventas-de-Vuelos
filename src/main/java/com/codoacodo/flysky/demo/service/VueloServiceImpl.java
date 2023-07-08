package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.Util;
import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.VentaDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;
import com.codoacodo.flysky.demo.exception.EntityNotFoundException;
import com.codoacodo.flysky.demo.exception.UnAuthorizedException;
import com.codoacodo.flysky.demo.model.entity.ButacaEntity;
import com.codoacodo.flysky.demo.model.entity.ReservaEntity;
import com.codoacodo.flysky.demo.model.entity.UsuarioEntity;
import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.codoacodo.flysky.demo.model.enums.TipoUsuario;
import com.codoacodo.flysky.demo.repository.IButacaRepository;
import com.codoacodo.flysky.demo.repository.IReservaRepository;
import com.codoacodo.flysky.demo.repository.IUsuarioRepository;
import com.codoacodo.flysky.demo.repository.IVueloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VueloServiceImpl implements IVueloService {
    private final IVueloRepository vueloRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IButacaRepository butacaRepository;

    private final IReservaRepository reservaRepository;

    public VueloServiceImpl(IVueloRepository vueloRepository, IUsuarioRepository usuarioRepository,
                            IButacaRepository butacaRepository, IReservaRepository reservaRepository) {
        this.vueloRepository = vueloRepository;
        this.usuarioRepository = usuarioRepository;
        this.butacaRepository = butacaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<VueloDto> obtenerVuelosDisponibles(String nombreUsuarioTipoCliente) {

        Optional<UsuarioEntity> usuario = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

        if (usuario.isEmpty()) {
            throw new NoSuchElementException("Usuario no registrado. Registrese como CLIENTE para poder visualizar " +
                    "vuelos disponibles.");
        }

        if (!usuario.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {
            throw new UnAuthorizedException("Usuario registrado pero NO AUTORIZADO para poder visualizar " +
                    "vuelos disponibles. Registrese como CLIENTE.");
        }

        List<VueloEntity> vuelosEntity = vueloRepository.findByDisponibleTrue();

        //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
        // una lista de vuelos vacía sin lanzar una excepción.
        if (vuelosEntity.isEmpty()) {
            throw new EntityNotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
        }

        ModelMapper mapper = new ModelMapper();

        List<VueloDto> vuelosDto = new ArrayList<>();
        vuelosEntity.forEach(vueloEntity -> vuelosDto.add(mapper.map(vueloEntity, VueloDto.class)));

        //Otra alternativa
        //List<VueloDto> vuelosDto = vuelosEntity.stream().map(vueloEntity-> mapper.map(vueloEntity, VueloDto.class)).toList();

        return vuelosDto;
    }


    @Override
    public ReservaDto reservarVuelo(String nombreUsuarioTipoCliente, ReservaVueloDto
            reservaVueloDto) {

        Optional<UsuarioEntity> usuario = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

        if (usuario.isPresent()) {
            //if (usuario.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Cliente"))
            if (usuario.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

                List<VueloEntity> vuelosDisponibles = vueloRepository.findByDisponibleTrue();
                //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
                // una lista de vuelos vacía sin lanzar una excepción.
                if (vuelosDisponibles.isEmpty()) {
                    throw new EntityNotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
                }

                Optional<VueloEntity> vueloDisponibleReserva = vuelosDisponibles.stream().filter(vueloDisponible ->
                        vueloDisponible.getNumeroVuelo().equals(reservaVueloDto.getNumeroVuelo()) &
                                vueloDisponible.getAerolinea().equalsIgnoreCase(reservaVueloDto.getAerolinea()) &
                                vueloDisponible.getFechaHoraPartida().equals(reservaVueloDto.getFechaHoraPartida()) &
                                vueloDisponible.getFechaHoraLlegada().equals(reservaVueloDto.getFechaHoraLlegada()) &
                                vueloDisponible.getOrigen().equalsIgnoreCase(reservaVueloDto.getOrigen()) &
                                vueloDisponible.getDestino().equalsIgnoreCase(reservaVueloDto.getDestino())
                ).findFirst();
                //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
                // DISPONIBLE Y NO PARA RELLENAR
                if (vueloDisponibleReserva.isEmpty()) {
                    throw new NoSuchElementException("El vuelo que quiere reservar no está disponible.");
                }

                List<ButacaEntity> butacasVueloDisponibleReserva = vueloDisponibleReserva.get().getButacas();

                if (butacasVueloDisponibleReserva.isEmpty()) {
                    throw new EntityNotFoundException("El vuelo que quiere reservar no tiene asignadas butacas.");
                }

                Optional<ButacaEntity> butacaVueloDisponibleReserva = butacasVueloDisponibleReserva.stream()
                        .filter(butaca -> reservaVueloDto.getPosicionButaca().equals(butaca.getPosicion()))
                        .findFirst();
                //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
                // DISPONIBLE Y NO PARA RELLENAR
                if (butacaVueloDisponibleReserva.isEmpty()) {
                    throw new NoSuchElementException("La posición de la butaca seleccionada no pertenece " +
                            "al vuelo.");
                }
                //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
                // DISPONIBLE Y NO PARA RELLENAR
                if (!butacaVueloDisponibleReserva.get().getDisponible()) {
                    throw new EntityNotFoundException("La posición de la butaca seleccionada no está disponible.");
                }

                //Modificamos por FALSE la disponibilidad de la butaca reservada.

                ButacaEntity butacaEntity = butacaRepository.save(crearButacaPersistencia(butacaVueloDisponibleReserva));

                //Modificamos por FALSE la disponibilidad del vuelo si todas las butacas han sido reservadas.
                List<ButacaEntity> butacasNoDisponibles = butacasVueloDisponibleReserva.stream()
                        .filter(butaca -> butaca.getDisponible().equals(Boolean.FALSE))
                        .toList();

                if (butacasNoDisponibles.size() == vueloDisponibleReserva.get().getCapacidad()) {

                   vueloRepository.save(crearVueloDisponibleReservaPersistencia(vueloDisponibleReserva, reservaVueloDto));
                }

                ReservaEntity reservaEntity = reservaRepository
                        .save(crearReservaEntityPersistencia(reservaVueloDto, vueloDisponibleReserva, usuario, butacaEntity));

                ModelMapper mapper = new ModelMapper();

                return mapper.map(reservaEntity, ReservaDto.class);

            }
            throw new UnAuthorizedException("Usuario registrado pero NO AUTORIZADO para poder realizar reservas. " +
                    "Registrese como CLIENTE.");
        }
        throw new NoSuchElementException("Usuario no registrado. Registrese como CLIENTE para poder realizar reservas.");
    }

    @Override
    public List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String
            nombreUsuarioTipoCliente) {
        ModelMapper mapper = new ModelMapper();

        //Si no manejamos con un Optional y usuarioAgente no existe, Spring nos lanza un NullPointerException(500 Internal Server Error)
        //Si lo manejamos con un Optional y usuarioAgente no existe, Spring nos lanza un NoSuchElementException(500 Internal Server Error)
        Optional<UsuarioEntity> usuarioAgente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAgente);

        if (usuarioAgente.isPresent()) {
            //if (usuarioAgente.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Agente de ventas"))
            if (usuarioAgente.get().getTipoUsuario().equals(TipoUsuario.AGENTE_DE_VENTAS)) {

                Optional<UsuarioEntity> usuarioCliente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

                if (usuarioCliente.isPresent()) {
                    if (usuarioCliente.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

                        List<ReservaEntity> reservasEntity = usuarioCliente.get().getReserva();

                        //Otra alternativa pero menos eficiente, ya que estoy llamando a dos repositorios para el mismo
                        // fin, es decir, pido dos veces las reservas.
                        //List<ReservaEntity> reservasEntity = reservaRepository.findByUsuario(usuarioCliente.get());

                        return reservasEntity.stream()
                                .map(reservaEntity -> mapper.map(reservaEntity, ReservaDto.class))
                                .toList();

                    }
                    throw new EntityNotFoundException("El usuario al que pretende visualizar sus reservas está registrado " +
                            "pero no como cliente por lo que no tiene reservas, ya que no las puede hacer.");
                }
                throw new NoSuchElementException("El usuario al que pretende visualizar sus reservas no está registrado.");
            }
            throw new UnAuthorizedException("Usuario registrado pero NO AUTORIZADO para poder visualizar el listado " +
                    "de reservas. Registrese como Agente de ventas.");
        }
        throw new NoSuchElementException("Usuario no registrado. Registrese como Agente de ventas para poder " +
                "visualizar el listado de reservas por cliente.");
    }

    @Override
    public VentaDto obtenerNumeroVentasIngresosDiarios(String nombreUsuarioTipoAdministrador, LocalDate fecha) {

        Optional<UsuarioEntity> usuarioAdministrador = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAdministrador);

        if (usuarioAdministrador.isEmpty()) {
            throw new NoSuchElementException("Usuario no registrado. Registrese como ADMINISTRADOR para poder visualizar " +
                    "el número de ventas e ingresos generados diarios.");
        }

        if (!usuarioAdministrador.get().getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
            throw new UnAuthorizedException("Usuario registrado pero NO AUTORIZADO para poder visualizar " +
                    "el número de ventas e ingresos generados diarios. Registrese como ADMINISTRADOR.");
        }

        List<ReservaEntity> reservasEntity = reservaRepository.findByFechaReserva(fecha);

        if (reservasEntity.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

            throw new EntityNotFoundException("No hay reservas realizadas el " + fecha.format(formatter) + ".");
        }

        VentaDto ventaDto = new VentaDto();
        ventaDto.setFecha(fecha);
        ventaDto.setCantidadVenta(reservasEntity.size());

        List<Double> montosPago = reservasEntity.stream().map(reservaEntity -> reservaEntity.getMontoPago()).toList();

        ventaDto.setIngreso(montosPago.stream()
                .reduce(0.0, (accum, montoPago) -> accum + montoPago)); //operación de reducción para realizar la suma

        //Otra alternativa using Lambda expression i -> i
        //ventaDto.setIngreso(montosPago.stream().mapToDouble(i-> i).sum());
        //Using method reference Double::doubleValue
        //ventaDto.setIngreso(montosPago.stream().mapToDouble(Double::doubleValue).sum());

        return ventaDto;
    }

    private ButacaEntity crearButacaPersistencia(Optional<ButacaEntity> butacaVueloDisponibleReserva) {
        ButacaEntity butacaPersitencia = new ButacaEntity();
        butacaPersitencia.setId(butacaVueloDisponibleReserva.get().getId());
        butacaPersitencia.setDisponible(Boolean.FALSE);
        butacaPersitencia.setPosicion(butacaVueloDisponibleReserva.get().getPosicion());
        butacaPersitencia.setVuelo(butacaVueloDisponibleReserva.get().getVuelo());

        return butacaPersitencia;
    }

    private VueloEntity crearVueloDisponibleReservaPersistencia(Optional<VueloEntity> vueloDisponibleReserva, ReservaVueloDto reservaVueloDto) {
        VueloEntity vueloDisponibleReservaPersistencia = new VueloEntity();
        vueloDisponibleReservaPersistencia.setId(vueloDisponibleReserva.get().getId());
        vueloDisponibleReservaPersistencia.setNumeroVuelo(reservaVueloDto.getNumeroVuelo());
        vueloDisponibleReservaPersistencia.setDisponible(Boolean.FALSE);
        vueloDisponibleReservaPersistencia.setCapacidad(vueloDisponibleReserva.get().getCapacidad());
        vueloDisponibleReservaPersistencia.setAerolinea(vueloDisponibleReserva.get().getAerolinea());
        vueloDisponibleReservaPersistencia.setFechaHoraPartida(vueloDisponibleReserva.get().getFechaHoraPartida());
        vueloDisponibleReservaPersistencia.setFechaHoraLlegada(vueloDisponibleReserva.get().getFechaHoraLlegada());
        vueloDisponibleReservaPersistencia.setPrecio(vueloDisponibleReserva.get().getPrecio());
        vueloDisponibleReservaPersistencia.setOrigen(vueloDisponibleReserva.get().getOrigen());
        vueloDisponibleReservaPersistencia.setDestino(vueloDisponibleReserva.get().getDestino());

        return vueloDisponibleReservaPersistencia;
    }

    private ReservaEntity crearReservaEntityPersistencia(ReservaVueloDto reservaVueloDto,
                                                         Optional<VueloEntity> vueloDisponibleReserva,
                                                         Optional<UsuarioEntity> usuario, ButacaEntity butacaEntity) {
        ReservaEntity reservaEntityPersistencia = new ReservaEntity();
        reservaEntityPersistencia.setTipoPago(reservaVueloDto.getTipoPago());
        reservaEntityPersistencia.setMontoPago
                (Util.montoAPagar(reservaVueloDto.getTipoPago(), vueloDisponibleReserva.get().getPrecio()));
        reservaEntityPersistencia.setFechaReserva(LocalDate.now());
        reservaEntityPersistencia.setUsuario(usuario.get());
        //Podemos utilizar vueloDisponibleReserva ya que sus valores fueron seteados a
        // vueloDisponibleReservaPersistencia, el cual no podemos utilizar porque está dentro de un if.
        reservaEntityPersistencia.setVuelo(vueloDisponibleReserva.get());
        reservaEntityPersistencia.setPosicionButaca(butacaEntity.getPosicion());

        return reservaEntityPersistencia;
    }
}