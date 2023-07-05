package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.Util;
import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.ReservaVueloResponseDto;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VueloServiceImpl implements IVueloService {
    private IVueloRepository vueloRepository;
    private IUsuarioRepository usuarioRepository;
    private IButacaRepository butacaRepository;

    private IReservaRepository reservaRepository;

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
        vuelosEntity.stream().forEach(vueloEntity -> vuelosDto.add(mapper.map(vueloEntity, VueloDto.class)));

        //Otra alternativa
        //List<VueloDto> vuelosDto = vuelosEntity.stream().map(vueloEntity-> mapper.map(vueloEntity, VueloDto.class)).toList();

        return vuelosDto;
    }


    @Override
    public ReservaVueloResponseDto reservarVuelo(String nombreUsuarioTipoCliente, ReservaVueloDto
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

                Optional<VueloEntity> vueloDisponibleReserva = vuelosDisponibles.stream()
                        .filter(vueloDisponible ->
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
                //id de la butaca a reservar.
                Long id = butacaVueloDisponibleReserva.get().getId();

                ButacaEntity butacaPersitencia = new ButacaEntity();
                butacaPersitencia.setId(id);
                butacaPersitencia.setDisponible(Boolean.FALSE);
                butacaPersitencia.setPosicion(butacaVueloDisponibleReserva.get().getPosicion());
                butacaPersitencia.setVuelo(butacaVueloDisponibleReserva.get().getVuelo());

                butacaRepository.save(butacaPersitencia);

                //Modificamos por FALSE la disponibilidad del vuelo si todas las butacas han sido reservadas.
                List<ButacaEntity> butacasNoDisponibles = butacasVueloDisponibleReserva.stream()
                        .filter(butaca -> butaca.getDisponible().equals(Boolean.FALSE))
                        .toList();


                if (butacasNoDisponibles.size() == vueloDisponibleReserva.get().getCapacidad()) {

                    VueloEntity vueloDisponibleReservaPersistencia = new VueloEntity();
                    vueloDisponibleReservaPersistencia.setId(vueloDisponibleReserva.get().getId());
                    vueloDisponibleReservaPersistencia.setDisponible(Boolean.FALSE);
                    vueloDisponibleReservaPersistencia.setCapacidad(vueloDisponibleReserva.get().getCapacidad());
                    vueloDisponibleReservaPersistencia.setAerolinea(vueloDisponibleReserva.get().getAerolinea());
                    vueloDisponibleReservaPersistencia.setFechaHoraPartida(vueloDisponibleReserva.get().getFechaHoraPartida());
                    vueloDisponibleReservaPersistencia.setFechaHoraLlegada(vueloDisponibleReserva.get().getFechaHoraLlegada());
                    vueloDisponibleReservaPersistencia.setPrecio(vueloDisponibleReserva.get().getPrecio());
                    vueloDisponibleReservaPersistencia.setOrigen(vueloDisponibleReserva.get().getOrigen());
                    vueloDisponibleReservaPersistencia.setDestino(vueloDisponibleReserva.get().getDestino());

                    vueloRepository.save(vueloDisponibleReservaPersistencia);
                }

                Double montoPago = Util.montoAPagar(reservaVueloDto.getTipoPago(), vueloDisponibleReserva.get().getPrecio());

                ReservaEntity reservaEntityPersistencia = new ReservaEntity();
                reservaEntityPersistencia.setTipoPago(reservaVueloDto.getTipoPago());
                reservaEntityPersistencia.setMontoPago(montoPago);
                reservaEntityPersistencia.setFechaReserva(LocalDate.now());
                reservaEntityPersistencia.setUsuario(usuario.get());
                //Podemos utilizar vueloDisponibleReserva ya que fueron seteados al mismo los valores nuevos
                // de vueloDisponibleReservaPersistencia
                reservaEntityPersistencia.setVuelo(vueloDisponibleReserva.get());

                reservaRepository.save(reservaEntityPersistencia);

                ReservaVueloResponseDto reservaVueloResponseDto = new ReservaVueloResponseDto();
                reservaVueloResponseDto.setNombreUsuario(reservaEntityPersistencia.getUsuario().getNombreUsuario());
                reservaVueloResponseDto.setAerolinea(reservaEntityPersistencia.getVuelo().getAerolinea());
                reservaVueloResponseDto.setFechaHoraPartida(reservaEntityPersistencia.getVuelo().getFechaHoraPartida());
                reservaVueloResponseDto.setFechaHoraLlegada(reservaEntityPersistencia.getVuelo().getFechaHoraLlegada());
                reservaVueloResponseDto.setOrigen(reservaEntityPersistencia.getVuelo().getOrigen());
                reservaVueloResponseDto.setDestino(reservaEntityPersistencia.getVuelo().getDestino());
                reservaVueloResponseDto.setPosicionButaca(butacaPersitencia.getPosicion());  //Por esta linea no utilizamos un ModelMapper.
                reservaVueloResponseDto.setTipoPago(reservaEntityPersistencia.getTipoPago());
                reservaVueloResponseDto.setMontoPago(reservaEntityPersistencia.getMontoPago());
                reservaVueloResponseDto.setFechaReserva(reservaEntityPersistencia.getFechaReserva());

                return reservaVueloResponseDto;

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

                        List<ReservaDto> reservasDto = reservasEntity.stream()
                                .map(reservaEntity -> mapper.map(reservaEntity, ReservaDto.class))
                                .toList();
                        return reservasDto; //Responder con otro DTO más específico.

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
            throw new EntityNotFoundException("No hay reservas realizadas el " + fecha + ".");
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

}
