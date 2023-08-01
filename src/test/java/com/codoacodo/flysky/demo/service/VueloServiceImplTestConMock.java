package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.exception.EntityNotFoundException;
import com.codoacodo.flysky.demo.model.entity.Butaca;
import com.codoacodo.flysky.demo.model.entity.Reserva;
import com.codoacodo.flysky.demo.model.entity.Usuario;
import com.codoacodo.flysky.demo.model.entity.Vuelo;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import com.codoacodo.flysky.demo.model.enums.TipoUsuario;
import com.codoacodo.flysky.demo.repository.IButacaRepository;
import com.codoacodo.flysky.demo.repository.IReservaRepository;
import com.codoacodo.flysky.demo.repository.IUsuarioRepository;
import com.codoacodo.flysky.demo.repository.IVueloRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class VueloServiceImplTestConMock {

    @Mock
    IUsuarioRepository usuarioRepository;

    @Mock
    IVueloRepository vueloRepository;

    @Mock
    IButacaRepository butacaRepository;

    @Mock
    IReservaRepository reservaRepository;


    @InjectMocks
    VueloServiceImpl vueloService;

    @Test
    @DisplayName("US1- Camino la lista de vuelos está vacía.")
    void obtenerVuelosDisponiblesThrowFirstEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Mariano"; //CLIENTE

        List<Reserva> reservasMock = new ArrayList<>();

        Optional<Usuario> usuarioMock = Optional
                .of(new Usuario(4L, "Mariano", TipoUsuario.CLIENTE, 666666, reservasMock));

        List<Vuelo> vuelosMock = new ArrayList<>();

        when(usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente)).thenReturn(usuarioMock);
        when(vueloRepository.findAll()).thenReturn(vuelosMock);
        //Al estar en producción los atributos de la inyeccion de dependencias en final no inyectaba los mock.

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US1- Camino no hay vuelos disponibles.")
    void obtenerVuelosDisponiblesThrowSecondEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Mariano"; //CLIENTE

        List<Reserva> reservasMock = new ArrayList<>();

        Optional<Usuario> usuarioMock = Optional
                .of(new Usuario(4L, "Mariano", TipoUsuario.CLIENTE, 666666, reservasMock));

        List<Butaca> butacasMock = new ArrayList<>();

        List<Vuelo> vuelosMock = new ArrayList<>();

        Vuelo vueloMock = new Vuelo(1L, 666, 156, "Aerolineas Argentinas",
                LocalDateTime.of(2050, 07, 25, 8, 00, 00),
                LocalDateTime.of(2050, 07, 25, 8, 45, 00), 15000D,
                "Buenos Aires", "Uruguay", reservasMock, butacasMock );

        vuelosMock.add(vueloMock);

        when(usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente)).thenReturn(usuarioMock);
        when(vueloRepository.findAll()).thenReturn(vuelosMock);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US5- Camino: no hay reservas realizadas.")
    void obtenerNumeroVentasIngresosDiariosThrowFirstEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoAdministrador = "Juan"; //ADMINISTRADOR
        LocalDate fecha = LocalDate.of(2023, 04, 25);

        List<Reserva> reservasMock = new ArrayList<>();

        Optional<Usuario> usuarioMock = Optional
                .of(new Usuario(3L, "Juan", TipoUsuario.ADMINISTRADOR, 984866, reservasMock));

        when(usuarioRepository.findByNombreUsuario(nombreUsuarioTipoAdministrador)).thenReturn(usuarioMock);
        when(reservaRepository.findAll()).thenReturn(reservasMock);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha);
        });
    }

}
