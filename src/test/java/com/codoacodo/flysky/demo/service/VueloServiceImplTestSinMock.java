package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
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

    //------------------------------------------------------------------------------------------------------------------
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

        vueloRepository.deleteAll();//Se puede utilizar porque estamos trabajando con una base de datos en memoria (H2)
        // para el entorno de test. Si ejecutamos todos los test simultaneamente debemos utilizar la anotación
        // @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD).
        // Si tenemos la propiedad fetch = FetchType.EAGER en el atributo butacas y/o reservas en VueloEntity, no se
        // borran los registros relacionados, por lo tanto, no pasa el test.

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("US2 y US3 - Camino feliz.")
    void reservarVueloOkTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Mariano"; //CLIENTE

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE05", TipoPago.PAGO_EN_LINEA);

        UsuarioDto usuario = new UsuarioDto("Mariano", 666666);

        VueloReservaDto vuelo = new VueloReservaDto(666, "Aerolineas Argentinas",
                LocalDateTime.of(2023, 06, 25, 23, 53, 30),
                LocalDateTime.of(2023, 06, 25, 23, 53, 30),
                "Buenos Aires", "Uruguay");

        ReservaDto expected = new ReservaDto(TipoPago.PAGO_EN_LINEA, 13500D, LocalDate.now(), usuario, vuelo
                , "AE05" );
        //ACT
        ReservaDto actual = vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);

        //ASSERT
        assertEquals(expected, actual);
        //Modificamos Persist por @ManyToOne(cascade = CascadeType.MERGE) tanto en el atributo usuario y vuelo de
        // ReservaEntity. https://www.baeldung.com/hibernate-detached-entity-passed-to-persist
        //Podemos ver que la entidad ReservaEntity tiene una relación de muchos a uno con vuelo y usuario. El tipo
        // de cascada se establece en CascadeType.MERGE; por lo tanto, solo propagaremos las operaciones de combinación
        // al vuelo y usuario asociado. En otras palabras, si fusionamos una entidad ReservaEntity, Hibernate propagará
        // la operación al vuelo y usuario asociado, y ambas entidades se actualizarán en la base de datos. Por el
        // contrario, si el tipo de cascada se establece en PERSIST o ALL, Hibernar intentará propagar la operación
        // persist en el campo asociado separado. En consecuencia, cuando persistimos una entidad vuelo y/o usuario
        // con uno de estos tipos en cascada, Hibernate conservará la ReservaEntity separado asociado, lo que dará lugar
        // a otra PersistentObjectException.
    }

    @Test
    @DisplayName("US2 y US3 - Camino usuario no registrado.")
    void reservarVueloThrowNoSuchElementUsuarioExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoCliente = "Fake";

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE05", TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino usuario registrado pero no autorizado para visualizar vuelos disponibles.")
    void reservarVueloThrowUnAuthorizedExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoCliente = "Carlos"; //AGENTE_DE_VENTAS
        String nombreUsuarioTipoCliente1 = "Juan"; //ADMINISTRADOR

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE05", TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });

        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino no hay vuelos disponibles.")
    @Disabled
    void reservarVueloThrowEntityNotFoundExceptionTest() {

        vueloRepository.deleteAll();//Se puede utilizar porque estamos trabajando con una base de datos en memoria (H2)
        // para el entorno de test. Si ejecutamos todos los test simultaneamente debemos utilizar la anotación
        // @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD).
        // Si tenemos la propiedad fetch = FetchType.EAGER en el atributo butacas y/o reservas en VueloEntity y en el
        // atributo reserva en UsuarioEntity no se borran los registros relacionados, por lo tanto, no pasa el test.

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE05", TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino el vuelo que quiere reservar no tiene asignadas butacas.")
    void reservarVueloThrowEntityNotFoundExceptionTest1() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //Intentamos reservar un vuelo disponible sin butacas asignadas.
        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(125, "Aerolineas Uruguayas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE05", TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino la posición de la butaca seleccionada no pertenece al vuelo.")
    void reservarVueloThrowNoSuchElementExceptionButacaTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //Intentamos reservar una butaca que no pertenece al vuelo disponible.
        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE08", TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino la posición de la butaca seleccionada no está disponible.")
    void reservarVueloThrowEntityNotFoundExceptionTest3() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //Intentamos reservar una butaca que ya está reservada.
        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, "Aerolineas Argentinas"
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , LocalDateTime.of(2023, 06, 25, 23, 53,30)
                , "Buenos Aires" , "Uruguay", "AE04", TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    //------------------------------------------------------------------------------------------------------------------

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

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("US5- Camino feliz.")
    void obtenerNumeroVentasIngresosDiariosOkTest() {
        //ARRANGE  LocalDate fecha
        String nombreUsuarioTipoAdministrador = "Juan"; //ADMINISTRADOR
        LocalDate fecha = LocalDate.of(2023, 04, 25);

        VentaDto expected = new VentaDto( LocalDate.of(2023, 04, 25), 1, 15000D);

        //ACT
        VentaDto actual = vueloService
                .obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha);

        //ASSERT
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("US5- Camino usuario no registrado.")
    void obtenerNumeroVentasIngresosDiariosThrowNoSuchElementExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoAdministrador = "Fake";
        LocalDate fecha = LocalDate.of(2023, 04, 25);

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha);
        });
    }

    @Test
    @DisplayName("US5- Camino: usuario registrado pero no autorizado para visualizar el número de ventas e ingresos " +
            "generados diarios.")
    void obtenerNumeroVentasIngresosDiariosThrowUnAuthorizedExceptionTest() {
        //ARRANGE
        String nombreUsuarioTipoAdministrador = "Miguel"; //CLIENTE
        String nombreUsuarioTipoAdministrador1 = "Carlos"; //AGENTE_DE_VENTAS
        LocalDate fecha = LocalDate.of(2023, 04, 25);

        //ACT and ASSERT
        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha);
        });

        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador1, fecha);
        });
    }

    @Test
    @DisplayName("US5- Camino: no hay reservas realizadas para tal fecha.")
    void obtenerNumeroVentasIngresosDiariosThrowEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoAdministrador = "Juan"; //ADMINISTRADOR
        //Buscamos por una fecha que no tiene reservas realizadas.
        LocalDate fecha = LocalDate.of(2023, 04, 26);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha);
        });
    }
}
