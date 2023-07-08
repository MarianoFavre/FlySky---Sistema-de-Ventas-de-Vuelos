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

        //Otra alternativa para tratar un Optional y lanzar una excepción.
        UsuarioEntity usuario = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente)
                .orElseThrow(() -> new NoSuchElementException("Usuario no registrado. Registrese como CLIENTE para " +
                        "poder realizar reservas."));

        //if (usuario.getTipoUsuario().getDescripcion().equalsIgnoreCase("Cliente"))
        if (usuario.getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

            List<VueloEntity> vuelosDisponibles = vueloRepository.findByDisponibleTrue();
            //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
            // una lista de vuelos vacía sin lanzar una excepción.
            if (vuelosDisponibles.isEmpty()) {
                throw new EntityNotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
            }

            VueloEntity vueloReserva = vuelosDisponibles.stream().filter(vueloDisponible ->
                    vueloDisponible.getNumeroVuelo().equals(reservaVueloDto.getNumeroVuelo()) &
                            vueloDisponible.getAerolinea().equalsIgnoreCase(reservaVueloDto.getAerolinea()) &
                            vueloDisponible.getFechaHoraPartida().equals(reservaVueloDto.getFechaHoraPartida()) &
                            vueloDisponible.getFechaHoraLlegada().equals(reservaVueloDto.getFechaHoraLlegada()) &
                            vueloDisponible.getOrigen().equalsIgnoreCase(reservaVueloDto.getOrigen()) &
                            vueloDisponible.getDestino().equalsIgnoreCase(reservaVueloDto.getDestino())
            ).findFirst()
                    .orElseThrow(()-> new NoSuchElementException("El vuelo que quiere reservar no está disponible."));
            //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
            // DISPONIBLE Y NO PARA RELLENAR.

            List<ButacaEntity> butacasVueloReserva = vueloReserva.getButacas();

            if (butacasVueloReserva.isEmpty()) {
                throw new EntityNotFoundException("El vuelo que quiere reservar no tiene asignadas butacas.");
            }

            ButacaEntity butacaVueloReserva = butacasVueloReserva.stream()
                    .filter(butaca -> reservaVueloDto.getPosicionButaca().equals(butaca.getPosicion()))
                    .findFirst()
                    .orElseThrow(()-> new NoSuchElementException("La posición de la butaca seleccionada no pertenece " +
                            "al vuelo.") );
            //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
            // DISPONIBLE Y NO PARA RELLENAR

            if (!butacaVueloReserva.getDisponible()) {
                throw new EntityNotFoundException("La posición de la butaca seleccionada no está disponible.");
            }

            //Modificamos por FALSE la disponibilidad de la butaca reservada.
            butacaVueloReserva.setDisponible(Boolean.FALSE);
            ButacaEntity butacaEntity = butacaRepository.save(butacaVueloReserva);

            //Modificamos por FALSE la disponibilidad del vuelo si todas las butacas han sido reservadas.
            Long cantidadButacasOcupadas = butacasVueloReserva.stream()
                    .filter(butaca -> butaca.getDisponible().equals(Boolean.FALSE))
                    .count();

            if (cantidadButacasOcupadas ==  vueloReserva.getCapacidad()) {

                vueloReserva.setDisponible(Boolean.FALSE);
                vueloRepository.save(vueloReserva);
            }

            ReservaEntity reservaEntity = reservaRepository
                    .save(crearReservaEntityPersistencia(reservaVueloDto, vueloReserva, usuario, butacaEntity));

            ModelMapper mapper = new ModelMapper();

            return mapper.map(reservaEntity, ReservaDto.class);

        }
        throw new UnAuthorizedException("Usuario registrado pero NO AUTORIZADO para poder realizar reservas. " +
                "Registrese como CLIENTE.");

    }

    @Override
    public List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String
            nombreUsuarioTipoCliente) {
        ModelMapper mapper = new ModelMapper();

        //Si no lo manejamos con un Optional y usuarioAgente no existe, Spring nos lanza un NullPointerException(500 Internal Server Error)
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
    private ReservaEntity crearReservaEntityPersistencia(ReservaVueloDto reservaVueloDto,
                                                         VueloEntity vueloReserva,
                                                         UsuarioEntity usuario, ButacaEntity butacaEntity) {
        ReservaEntity reservaEntityPersistencia = new ReservaEntity();
        reservaEntityPersistencia.setTipoPago(reservaVueloDto.getTipoPago());
        reservaEntityPersistencia.setMontoPago
                (Util.montoAPagar(reservaVueloDto.getTipoPago(), vueloReserva.getPrecio()));
        reservaEntityPersistencia.setFechaReserva(LocalDate.now());
        reservaEntityPersistencia.setUsuario(usuario);
        reservaEntityPersistencia.setVuelo(vueloReserva);
        reservaEntityPersistencia.setPosicionButaca(butacaEntity.getPosicion());

        return reservaEntityPersistencia;
    }
}