package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.exception.EntityNotFoundException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
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
    @DisplayName("US1- Camino no hay vuelos disponibles.")
    @Disabled
    void obtenerVuelosDisponiblesThrowEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Mariano"; //CLIENTE

        List<Reserva> reservaEntities = new ArrayList<>();

        Optional<Usuario> usuarioMock = Optional
                .of(new Usuario(4L, "Mariano", TipoUsuario.CLIENTE, 666666, reservaEntities));

        System.out.println(usuarioMock);

        List<Vuelo> vuelosMock = new ArrayList<>();

        when(usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente)).thenReturn(usuarioMock);
        when(vueloRepository.findByDisponibleTrue()).thenReturn(vuelosMock);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });

    }

    @Test
    @DisplayName("US2 y US3 - Camino no hay vuelos disponibles.")
    @Disabled
    void reservarVueloThrowEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53, 30)
                , LocalDateTime.of(2023, 06, 25, 23, 53, 30)
                , "Buenos Aires", "Uruguay", "AE05", TipoPago.PAGO_EN_LINEA);

        Usuario usuarioClienteMock =
                new Usuario(1L, "Miguel", TipoUsuario.CLIENTE, 156453, null);

        List<Vuelo> vuelosMock = new ArrayList<>();

        when(usuarioRepository.findByNombreUsuario(nombreUsuarioTipoCliente)).thenReturn(Optional.of(usuarioClienteMock));
        when(vueloRepository.findByDisponibleTrue()).thenReturn(vuelosMock);


        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }
}
