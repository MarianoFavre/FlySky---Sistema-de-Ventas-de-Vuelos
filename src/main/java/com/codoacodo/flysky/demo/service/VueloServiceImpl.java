package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;
import com.codoacodo.flysky.demo.exception.VueloNotFoundException;
import com.codoacodo.flysky.demo.model.entity.ButacaEntity;
import com.codoacodo.flysky.demo.model.entity.UsuarioEntity;
import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.codoacodo.flysky.demo.repository.IButacaRepository;
import com.codoacodo.flysky.demo.repository.IUsuarioRepository;
import com.codoacodo.flysky.demo.repository.IVueloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VueloServiceImpl implements IVueloService {
    private IVueloRepository vueloRepository;
    private IUsuarioRepository usuarioRepository;

    private IButacaRepository butacaRepository;

    public VueloServiceImpl(IVueloRepository vueloRepository, IUsuarioRepository usuarioRepository, IButacaRepository butacaRepository) {
        this.vueloRepository = vueloRepository;
        this.usuarioRepository = usuarioRepository;
        this.butacaRepository = butacaRepository;
    }


    @Override
    public List<VueloDto> obtenerVuelosDisponibles() {

        List<VueloEntity> vuelosEntity = vueloRepository.findByDisponibleTrue();

        //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
        // una lista de vuelos vacía sin lanzar una excepción.
        if (vuelosEntity.isEmpty()) {
            throw new VueloNotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
        }

        ModelMapper mapper = new ModelMapper();

        List<VueloDto> vuelosDto = new ArrayList<>();
        vuelosEntity.stream().forEach(vueloEntity -> vuelosDto.add(mapper.map(vueloEntity, VueloDto.class)));

        //Otra alternativa
        //List<VueloDto> vuelosDto = vuelosEntity.stream().map(vueloEntity-> mapper.map(vueloEntity, VueloDto.class)).toList();

        return vuelosDto;
    }

    @Override
    public ReservaVueloDto reservarVuelo(String nombreUsuario, int telefono, ReservaVueloDto reservaVueloDto) {

        Optional<UsuarioEntity> usuario = usuarioRepository.findByNombreUsuarioAndTelefono(nombreUsuario, telefono);

        if (usuario.isPresent()) {
            if (usuario.get().getTipoUsuario().getDescripcion().equalsIgnoreCase("Cliente")) {

                List<VueloEntity> vuelosEntity = vueloRepository.findByDisponibleTrue();
                //si la disponibilidad de todos los registro de la tabla vuelo es false, la base de datos va a retornar
                // una lista de vuelos vacía sin lanzar una excepción.
                if (vuelosEntity.isEmpty()) {
                    throw new VueloNotFoundException("No hay vuelos disponibles en este momento. Intente más tarde.");
                }

                Optional<VueloEntity> vuelo = vuelosEntity.stream()
                        .filter(vueloEntity -> vueloEntity.getAerolinea().equalsIgnoreCase(reservaVueloDto.getAerolinea()) &
                                vueloEntity.getFechaHoraPartida().equals(reservaVueloDto.getFechaHoraPartida()) &
                                vueloEntity.getFechaHoraLlegada().equals(reservaVueloDto.getFechaHoraLlegada()) &
                                vueloEntity.getOrigen().equalsIgnoreCase(reservaVueloDto.getOrigen()) &
                                vueloEntity.getDestino().equalsIgnoreCase(reservaVueloDto.getDestino())
                        ).findFirst();

                if (vuelo.isEmpty()) {
                    throw new VueloNotFoundException("El vuelo que quiere reservar no existe.");
                }

                if (vuelo.get().getDisponible().equals(Boolean.FALSE)) {
                    throw new VueloNotFoundException("El vuelo que quiere reservar no está disponible. Todas las " +
                            "butacas ya fueron reservadas");
                }

                List<ButacaEntity> butacasVuelo = vuelo.get().getButacas();

                List<ButacaEntity> butacasDisponibleReservaVuelo = butacasVuelo.stream()
                        .filter(butaca -> butaca.getDisponible().equals(Boolean.TRUE))
                        .toList();

                //butacasDisponibleReservaVuelo.

                return reservaVueloDto; //sacar


            }
            throw new RuntimeException("Ususario registrado pero no habilitado para poder realizar reservas. Registrese como CLIENTE.");
        }
        throw new RuntimeException("Usuario no registrado. Registrase como CLIENTE para poder realizar reservas.");
    }
}
