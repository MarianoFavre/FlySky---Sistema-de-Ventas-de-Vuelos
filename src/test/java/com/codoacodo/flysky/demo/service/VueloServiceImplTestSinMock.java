package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.response.*;
import com.codoacodo.flysky.demo.exception.EntityNotFoundException;
import com.codoacodo.flysky.demo.exception.UnAuthorizedException;
import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import com.codoacodo.flysky.demo.repository.IButacaRepository;
import com.codoacodo.flysky.demo.repository.IReservaRepository;
import com.codoacodo.flysky.demo.repository.IUsuarioRepository;
import com.codoacodo.flysky.demo.repository.IVueloRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//Mantiene el contexto inicial del repositorio después de ejecutar cada test
public class VueloServiceImplTestSinMock {

    @Autowired
    IVueloService vueloService;

    @Autowired
    IButacaRepository butacaRepository;

    @Autowired
    IReservaRepository reservaRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Autowired
    IVueloRepository vueloRepository;

    @Test
    @DisplayName("US1- Camino feliz.")
    void obtenerVuelosDisponiblesOkTest() {
        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<VueloDto> expected = new ArrayList<>();

        List<ButacaDto> butacaDtos1 = new ArrayList<>();
        ButacaDto butacaDto1 = new ButacaDto(FALSE, "AE04");
        ButacaDto butacaDto2 = new ButacaDto(TRUE, "AE05");
        ButacaDto butacaDto3 = new ButacaDto(TRUE, "AE06");
        butacaDtos1.add(butacaDto1);
        butacaDtos1.add(butacaDto2);
        butacaDtos1.add(butacaDto3);

        List<ButacaDto> butacaDtos2 = new ArrayList<>();

        VueloDto vueloDto1 = new VueloDto(666, TRUE, 3, "Aerolineas Argentinas",
                LocalDateTime.of(2023, 06, 25, 23, 53, 30),
                LocalDateTime.of(2023, 06, 25, 23, 53, 30), 15000D,
                "Buenos Aires", "Uruguay", butacaDtos1);
        VueloDto vueloDto2 = new VueloDto(125, TRUE, 50, "Aerolineas Uruguayas",
                LocalDateTime.of(2023, 06, 25, 23, 53, 30),
                LocalDateTime.of(2023, 06, 25, 23, 53, 30), 15000D,
                "Buenos Aires", "Uruguay", butacaDtos2);

        expected.add(vueloDto1);
        expected.add(vueloDto2);

        //ACT
        List<VueloDto> actual = vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);

        //ASSERT
        assertEquals(expected, actual);
        //Si lo ejecutamos asi ocurre lo siguiente :Caused by: org.hibernate.LazyInitializationException: failed to
        // lazily initialize a collection of role:com.codoacodo.flysky.demo.model.entity.VueloEntity.butacas:
        // could not initialize proxy - no Session.
        //DEBEMOS AGREGAR EN LA ENTIDAD VueloEntity en el atributo butacas la propiedad fetch = FetchType.EAGER.
        //https://www.baeldung.com/hibernate-initialize-proxy-exception

    }

    @Test
    @DisplayName("US1- Camino usuario no registrado.")
    void obtenerVuelosDisponiblesThrowNoSuchElementExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoCliente = "Fake";

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US1- Camino usuario registrado pero no autorizado para visualizar vuelos disponibles.")
    void obtenerVuelosDisponiblesThrowUnAuthorizedExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoCliente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente1 = "Juan"; //ADMINISTRADOR

        //ACT and ASSERT
        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });

        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente1);
        });
    }

    @Test
    @DisplayName("US1- Camino no hay vuelos disponibles.")
    @Disabled
    void obtenerVuelosDisponiblesThrowEntityNotFoundExceptionTest() {

        //vueloRepository.deleteAll();//No borra registros relacionados por lo tanto no pasa el test.
        //Se puede utilizar porque estamos trabajando con una base de datos en memoria (H2)
        // para el entorno de test. Si ejecutamos todos los test simultaneamente debemos utilizar la anotación
        // @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD).

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US4- Camino feliz.")
    void obtenerReservasPorNombreUsuarioOkTest() {
        //ARRANGE
        String nombreUsuarioTipoAgente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<ReservaDto> expected = new ArrayList<>();

        UsuarioDto usuarioDto = new UsuarioDto("Miguel", 156453);

        VueloReservaDto vueloReservaDto = new VueloReservaDto(666, "Aerolineas Argentinas",
                LocalDateTime.of(2023, 06, 25, 23, 53, 30),
                LocalDateTime.of(2023, 06, 25, 23, 53, 30), "Buenos Aires",
                "Uruguay");

        ReservaDto reservaDto = new ReservaDto(TipoPago.TRANSFERENCIA_BANCARIA, 15000D,
                LocalDate.of(2023, 04, 25), usuarioDto, vueloReservaDto, "AE04");

        expected.add(reservaDto);

        //ACT

        List<ReservaDto> actual = vueloService
                .obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);

        //ASSERT
        assertEquals(expected, actual);
        //Si lo ejecutamos asi ocurre lo siguiente :org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
        // com.codoacodo.flysky.demo.model.entity.UsuarioEntity.reserva: could not initialize proxy - no Session
        //DEBEMOS AGREGAR EN LA ENTIDAD UsuarioEntity en el atributo reserva la propiedad fetch = FetchType.EAGER.
    }

    @Test
    @DisplayName("US4- Camino usuario no registrado.")
    void obtenerReservasPorNombreUsuarioThrowNoSuchElementExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoAgente = "Fake";
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US4- Camino: usuario registrado pero no autorizado para visualizar el listado de reservas.")
    void obtenerReservasPorNombreUsuarioThrowUnAuthorizedExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoAgente = "Juan"; //ADMINISTRADOR
        String nombreUsuarioTipoAgente1 = "Miguel"; //CLIENTE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //ACT and ASSERT
        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);
        });

        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente1, nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US4- Camino: usuario al que pretende visualizar sus reservas no está registrado.")
    void obtenerReservasPorNombreUsuarioThrowNoSuchElementExceptionTipoClienteTest() {

        //ARRANGE
        String nombreUsuarioTipoAgente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente = "Fake";

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US4- Camino: usuario al que pretende visualizar sus reservas está registrado pero no como cliente.")
    void obtenerReservasPorNombreUsuarioThrowEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoAgente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente1 = "Juan"; //ADMINISTRADOR

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);
        });

        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente1);
        });
    }

    @Test
    @DisplayName("US4- Camino: no hay reservas realizadas por el usuario.")
    void obtenerReservasPorNombreUsuarioThrowEntityNotFoundExceptionTipoClienteTest() {
        //ARRANGE
        String nombreUsuarioTipoAgente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente = "Mariano"; //CLIENTE

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);
        });
    }
}
