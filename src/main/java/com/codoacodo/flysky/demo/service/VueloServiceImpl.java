package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.Util;
import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.ReservaVueloResponseDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;
import com.codoacodo.flysky.demo.exception.NotFoundException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<VueloDto> obtenerVuelosDisponibles() {

        List<VueloEntity> vuelosEntity = vueloRepository.findByDisponibleTrue();

        //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
        // una lista de vuelos vacía sin lanzar una excepción.
        if (vuelosEntity.isEmpty()) {
            throw new NotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
        }

        ModelMapper mapper = new ModelMapper();

        List<VueloDto> vuelosDto = new ArrayList<>();
        vuelosEntity.stream().forEach(vueloEntity -> vuelosDto.add(mapper.map(vueloEntity, VueloDto.class)));

        //Otra alternativa
        //List<VueloDto> vuelosDto = vuelosEntity.stream().map(vueloEntity-> mapper.map(vueloEntity, VueloDto.class)).toList();

        return vuelosDto;
    }

    @Override
    public ReservaVueloResponseDto reservarVuelo(String nombreUsuario, ReservaVueloDto reservaVueloDto) {

        Optional<UsuarioEntity> usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);

        if (usuario.isPresent()) {
            //if (usuario.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Cliente"))
            if (usuario.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

                List<VueloEntity> vuelosDisponibles = vueloRepository.findByDisponibleTrue();
                //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
                // una lista de vuelos vacía sin lanzar una excepción.
                if (vuelosDisponibles.isEmpty()) {
                    throw new NotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
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
                    throw new NotFoundException("El vuelo que quiere reservar no existe entre los vuelos disponibles.");
                }

                List<ButacaEntity> butacasVueloDisponibleReserva = vueloDisponibleReserva.get().getButacas();

                if (butacasVueloDisponibleReserva.isEmpty()) {
                    throw new NotFoundException("El vuelo que quiere reservar no tiene asignadas butacas.");
                }

                Optional<ButacaEntity> butacaVueloDisponibleReserva = butacasVueloDisponibleReserva.stream()
                        .filter(butaca -> reservaVueloDto.getPosicionButaca().equals(butaca.getPosicion()))
                        .findFirst();
                //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
                // DISPONIBLE Y NO PARA RELLENAR
                if (butacaVueloDisponibleReserva.isEmpty()) {
                    throw new NotFoundException("La posición de la butaca seleccionada no pertenece " +
                            "al vuelo.");
                }
                //PREGUNTAR SI SE DEBE HACER PORQUE EN EL FRONT TENDRIAMOS OPCIONES PARA SELECCIONAR DE LO QUE HAY
                // DISPONIBLE Y NO PARA RELLENAR
                if (!butacaVueloDisponibleReserva.get().getDisponible()) {
                    throw new NotFoundException("La posición de la butaca seleccionada no está disponible.");
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
                reservaEntityPersistencia.setFechaHoraReserva(LocalDateTime.now());
                reservaEntityPersistencia.setUsuario(usuario.get());
                reservaEntityPersistencia.setVuelo(vueloDisponibleReserva.get());

                ReservaEntity reservaEntity = reservaRepository.save(reservaEntityPersistencia);

                ReservaVueloResponseDto reservaVueloResponseDto = new ReservaVueloResponseDto();
                reservaVueloResponseDto.setNombreUsuario(reservaEntityPersistencia.getUsuario().getNombreUsuario());
                reservaVueloResponseDto.setAerolinea(reservaEntityPersistencia.getVuelo().getAerolinea());
                reservaVueloResponseDto.setFechaHoraPartida(reservaEntityPersistencia.getVuelo().getFechaHoraPartida());
                reservaVueloResponseDto.setFechaHoraLlegada(reservaEntityPersistencia.getVuelo().getFechaHoraLlegada());
                reservaVueloResponseDto.setOrigen(reservaEntityPersistencia.getVuelo().getOrigen());
                reservaVueloResponseDto.setDestino(reservaEntityPersistencia.getVuelo().getDestino());
                reservaVueloResponseDto.setPosicionButaca(butacaPersitencia.getPosicion());
                reservaVueloResponseDto.setTipoPago(reservaEntityPersistencia.getTipoPago());
                reservaVueloResponseDto.setMontoPago(reservaEntityPersistencia.getMontoPago());
                reservaVueloResponseDto.setFechaHoraReserva(reservaEntityPersistencia.getFechaHoraReserva());

                return reservaVueloResponseDto;

            }
            throw new UnAuthorizedException("Usuario registrado pero no habilitado para poder realizar reservas. " +
                    "Registrese como CLIENTE.");
        }
        throw new UnAuthorizedException("Usuario no registrado. Registrese como CLIENTE para poder realizar reservas.");
    }
/*
    @Override
    public List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String nombreUsuarioTipoCliente) {
        ModelMapper mapper = new ModelMapper();

        Optional<UsuarioEntity> usuarioAgente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAgente);

        if (usuarioAgente.isPresent()) {
            //if (usuarioAgente.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Agente de ventas"))
            if (usuarioAgente.get().getTipoUsuario().equals(TipoUsuario.AGENTE_DE_VENTAS)) {

                Optional<UsuarioEntity> usuarioCliente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

                if (usuarioCliente.isPresent()) {
                    if (usuarioCliente.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

                        List<ReservaEntity> reservasEntity = reservaRepository.findByUsuario(usuarioCliente.get());
                        List<ReservaDto> reservasDto = reservasEntity.stream()
                                .map(reservaEntity -> mapper.map(reservaEntity, ReservaDto.class))
                                .toList();
                        return reservasDto;

                    }
                    throw new NotFoundException("El usuario al que pretende visualizar sus reservas está registrado " +
                            "pero no como cliente por lo que no tiene reservas, ya que no las puede hacer.");
                }
                throw new NotFoundException("El usuario al que pretende visualizar sus reservas no está registrado.");
            }
            throw new UnAuthorizedException("Usuario registrado pero no habilitado para poder visualizar el listado " +
                    "de reservas. Registrese como Agente de ventas.");
        }
        throw new NotFoundException("Usuario no registrado. Registrese como Agente de ventas para poder " +
                "visualizar el listado de reservas por cliente.");
    }
*/
    @Override
    public List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String nombreUsuarioTipoCliente) {
        ModelMapper mapper = new ModelMapper();

        Optional<UsuarioEntity> usuarioAgente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAgente);


            //if (usuarioAgente.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Agente de ventas"))
            if (usuarioAgente.get().getTipoUsuario().equals(TipoUsuario.AGENTE_DE_VENTAS)) {

                Optional<UsuarioEntity> usuarioCliente = usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente);

                if (usuarioCliente.isPresent()) {
                    if (usuarioCliente.get().getTipoUsuario().equals(TipoUsuario.CLIENTE)) {

                        List<ReservaEntity> reservasEntity = reservaRepository.findByUsuario(usuarioCliente.get());
                        List<ReservaDto> reservasDto = reservasEntity.stream()
                                .map(reservaEntity -> mapper.map(reservaEntity, ReservaDto.class))
                                .toList();
                        return reservasDto;

                    }
                    throw new NotFoundException("El usuario al que pretende visualizar sus reservas está registrado " +
                            "pero no como cliente por lo que no tiene reservas, ya que no las puede hacer.");
                }
                throw new NotFoundException("El usuario al que pretende visualizar sus reservas no está registrado.");
            }
            throw new UnAuthorizedException("Usuario registrado pero no habilitado para poder visualizar el listado " +
                    "de reservas. Registrese como Agente de ventas.");
        }

}
