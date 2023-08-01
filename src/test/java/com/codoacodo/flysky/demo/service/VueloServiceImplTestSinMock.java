package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.*;
import com.codoacodo.flysky.demo.exception.EntityNotFoundException;
import com.codoacodo.flysky.demo.exception.UnAuthorizedException;
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

        List<ButacaDto> butacaDtos = new ArrayList<>();
        ButacaDto butacaDto1 = new ButacaDto(FALSE, "A1");
        ButacaDto butacaDto2 = new ButacaDto(TRUE, "B1");
        ButacaDto butacaDto3 = new ButacaDto(TRUE, "C1");
        ButacaDto butacaDto4 = new ButacaDto(TRUE, "D1");
        ButacaDto butacaDto5 = new ButacaDto(TRUE, "A2");
        ButacaDto butacaDto6 = new ButacaDto(TRUE, "B2");
        ButacaDto butacaDto7 = new ButacaDto(TRUE, "C2");
        ButacaDto butacaDto8 = new ButacaDto(TRUE, "D2");
        ButacaDto butacaDto9 = new ButacaDto(TRUE, "A3");
        ButacaDto butacaDto10 = new ButacaDto(TRUE, "B3");
        ButacaDto butacaDto11 = new ButacaDto(TRUE, "C3");
        ButacaDto butacaDto12 = new ButacaDto(TRUE, "D3");
        ButacaDto butacaDto13 = new ButacaDto(TRUE, "A5");
        ButacaDto butacaDto14 = new ButacaDto(TRUE, "B5");
        ButacaDto butacaDto15 = new ButacaDto(TRUE, "C5");
        ButacaDto butacaDto16 = new ButacaDto(TRUE, "D5");
        ButacaDto butacaDto17 = new ButacaDto(TRUE, "E5");
        ButacaDto butacaDto18 = new ButacaDto(TRUE, "F5");
        ButacaDto butacaDto19 = new ButacaDto(TRUE, "A28");
        ButacaDto butacaDto20 = new ButacaDto(TRUE, "B28");
        ButacaDto butacaDto21 = new ButacaDto(TRUE, "C28");
        ButacaDto butacaDto22 = new ButacaDto(TRUE, "D28");
        ButacaDto butacaDto23 = new ButacaDto(TRUE, "E28");
        ButacaDto butacaDto24 = new ButacaDto(TRUE, "F28");

        butacaDtos.add(butacaDto1);
        butacaDtos.add(butacaDto2);
        butacaDtos.add(butacaDto3);
        butacaDtos.add(butacaDto4);
        butacaDtos.add(butacaDto5);
        butacaDtos.add(butacaDto6);
        butacaDtos.add(butacaDto7);
        butacaDtos.add(butacaDto8);
        butacaDtos.add(butacaDto9);
        butacaDtos.add(butacaDto10);
        butacaDtos.add(butacaDto11);
        butacaDtos.add(butacaDto12);
        butacaDtos.add(butacaDto13);
        butacaDtos.add(butacaDto14);
        butacaDtos.add(butacaDto15);
        butacaDtos.add(butacaDto16);
        butacaDtos.add(butacaDto17);
        butacaDtos.add(butacaDto18);
        butacaDtos.add(butacaDto19);
        butacaDtos.add(butacaDto20);
        butacaDtos.add(butacaDto21);
        butacaDtos.add(butacaDto22);
        butacaDtos.add(butacaDto23);
        butacaDtos.add(butacaDto24);

        VueloDto vueloDto1 = new VueloDto(666, 156, "Aerolineas Argentinas",
                LocalDateTime.of(2050, 07, 25, 8, 00, 00),
                LocalDateTime.of(2050, 07, 25, 8, 45, 00), 15000D,
                "Buenos Aires", "Uruguay", butacaDtos);

        expected.add(vueloDto1);

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
    @DisplayName("US1- Camino la lista de vuelos está vacía.")
    @Disabled
    void obtenerVuelosDisponiblesThrowFirstEntityNotFoundExceptionTest() {
        //Orden de borrado siempre y cuando no se utilice fetch = FetchType.EAGER
        //butacaRepository.deleteAll(); //No utilizar fetch = FetchType.EAGER en la entidades relacionadas a Butaca (Reserva y Vuelo)
        //usuarioRepository.deleteAll();//Agregar CascadeType.REMOVE en el atributo butacas en la entidad Reserva para borrar las reservas del usuario.
        //reservaRepository.deleteAll(); //Si pretendemos borrar todas las reservas
        //vueloRepository.deleteAll();
        //Se puede utilizar porque estamos trabajando con una base de datos en memoria (H2) para el entorno de test.
        // Si ejecutamos todos los test simultaneamente debemos utilizar la anotación
        // @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD).
        //Al borrar el repositorio Usuario siempre nos va a lanzar  NoSuchElementException por lo que el test no va a pasar.
        //DEBEMOS UTILIZAR TEST CON MOCK PARA TESTEAR ESTE METODO.

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente);
        });
    }

    @Test
    @DisplayName("US1- Camino no hay vuelos disponibles.")
    @Disabled
    void obtenerVuelosDisponiblesThrowSecondEntityNotFoundExceptionTest() {
        butacaRepository.deleteAll();
        //Se puede utilizar porque estamos trabajando con una base de datos en memoria (H2) para el entorno de test.
        // Si ejecutamos todos los test simultaneamente debemos utilizar la anotación
        // @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD).
        // Si tenemos la propiedad fetch = FetchType.EAGER en el atributo butacas en Vuelo y en Reservas , no se
        // borran los registros, por lo tanto, no pasa el test. Pero si le sacamos dicha propiedad o utilizamos
        // fetch = FetchType.LAZY nos lanza la excepción LazyInitializationException.
        //DEBEMOS UTILIZAR TEST CON MOCK PARA TESTEAR ESTE METODO.

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

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, butacas, TipoPago.PAGO_EN_LINEA);

        VueloReservaDto vuelo = new VueloReservaDto(666, "Aerolineas Argentinas",
                LocalDateTime.of(2050, 07, 25, 8, 00, 00),
                LocalDateTime.of(2050, 07, 25, 8, 45, 00),
                "Buenos Aires", "Uruguay");

        ReservaDto expected = new ReservaDto(TipoPago.PAGO_EN_LINEA, 27000D, LocalDateTime.now(), vuelo
                , butacas);
        //ACT
        ReservaDto actual = vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);

        //ASSERT
        //assertEquals(expected, actual); //No pasa el test porque hay un defasaje entre la fecha real de reserva y la esperada
        assertEquals(expected.getTipoPago(), actual.getTipoPago());
        assertEquals(expected.getMontoPago(), actual.getMontoPago());
        assertEquals(expected.getVuelo(), actual.getVuelo());
        assertEquals(expected.getButacas(), actual.getButacas());
        //assertEquals(expected.getFechaReserva(), actual.getFechaReserva());
        //Modificamos Persist por @ManyToOne(cascade = CascadeType.MERGE) tanto en el atributo usuario y vuelo de
        // Reserva. https://www.baeldung.com/hibernate-detached-entity-passed-to-persist
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

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, butacas, TipoPago.PAGO_EN_LINEA);

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

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, butacas, TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });

        assertThrows(UnAuthorizedException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente1, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino el vuelo que quiere reservar no existe.")
    void reservarVueloThrowNoSuchElementExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);
        //Intentamos reservar un vuelo que no existe
        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(667, butacas, TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino el vuelo que intenta reservar ya caducó.")
    void reservarVueloThrowIllegalArgumentExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);
        //Intentamos reservar un vuelo que ya caducó
        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(934, butacas, TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino el vuelo que quiere reservar no tiene asignadas butacas.")
    void reservarVueloThrowEntityNotFoundExceptionTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);
        //Intentamos reservar un vuelo que no tiene asignadas butacas.
        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(578, butacas, TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }


    @Test
    @DisplayName("US2 y US3 - Camino la posición de la butaca que intenta reservar no pertenece al vuelo.")
    void reservarVueloThrowNoSuchElementExceptionButacaTest() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("D1", "Mariano");
        //Intentamos reservar una butaca que no pertenece al vuelo disponible.
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("E1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, butacas, TipoPago.PAGO_EN_LINEA);

        //ACT and ASSERT
        assertThrows(NoSuchElementException.class, () -> {
            vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto);
        });
    }

    @Test
    @DisplayName("US2 y US3 - Camino la posición de la butaca que intenta reservar ya se encuentra ocupada.")
    void reservarVueloThrowEntityNotFoundExceptionTest3() {

        //ARRANGE
        String nombreUsuarioTipoCliente = "Miguel"; //CLIENTE

        List<ButacaReservaDto> butacas = new ArrayList<>();
        //Intentamos reservar una butaca que ya está reservada.
        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("A1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("B1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto(666, butacas, TipoPago.PAGO_EN_LINEA);

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

        VueloReservaDto vueloReservaDto = new VueloReservaDto(666, "Aerolineas Argentinas",
                LocalDateTime.of(2050, 07, 25, 8, 00, 00),
                LocalDateTime.of(2050, 07, 25, 8, 45, 00), "Buenos Aires",
                "Uruguay");

        List<ButacaReservaDto> butacas = new ArrayList<>();
        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("A1", "Miguel");
        butacas.add(butacaReservaDto);

        ReservaDto reservaDto = new ReservaDto(TipoPago.TRANSFERENCIA_BANCARIA, 15000D,
                LocalDateTime.of(2023, 04, 25, 13, 45, 12), vueloReservaDto
                , butacas);

        expected.add(reservaDto);

        //ACT

        List<ReservaDto> actual = vueloService
                .obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente, nombreUsuarioTipoCliente);

        //ASSERT
        assertEquals(expected, actual);
        //Si lo ejecutamos asi ocurre lo siguiente :org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
        // com.codoacodo.flysky.demo.model.entity.UsuarioEntity.reserva: could not initialize proxy - no Session
        //DEBEMOS AGREGAR EN LA ENTIDAD Usuario en el atributo reserva la propiedad fetch = FetchType.EAGER.
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

        VentaDto expected = new VentaDto( 1, 15000D);

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
    @DisplayName("US5- Camino: no hay reservas realizadas.")
    @Disabled
    void obtenerNumeroVentasIngresosDiariosThrowFirstEntityNotFoundExceptionTest() {
        //Orden de borrado siempre y cuando no se utilice fetch = FetchType.EAGER
        //butacaRepository.deleteAll(); //No utilizar fetch = FetchType.EAGER en la entidades relacionadas a Butaca (Reserva y Vuelo)
        //usuarioRepository.deleteAll();//Agregar CascadeType.REMOVE en el atributo butacas en la entidad Reserva para borrar las reservas del usuario.
        //reservaRepository.deleteAll(); //Si pretendemos borrar todas las reservas
        //vueloRepository.deleteAll();
        //Se puede utilizar porque estamos trabajando con una base de datos en memoria (H2) para el entorno de test.
        // Si ejecutamos todos los test simultaneamente debemos utilizar la anotación
        // @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD).
        //Al borrar el repositorio Usuario siempre nos va a lanzar  NoSuchElementException por lo que el test no va a pasar.
        //DEBEMOS UTILIZAR TEST CON MOCK PARA TESTEAR ESTE METODO.

        //ARRANGE
        String nombreUsuarioTipoAdministrador = "Juan"; //ADMINISTRADOR
        LocalDate fecha = LocalDate.of(2023, 04, 25);

        //ACT and ASSERT
        assertThrows(EntityNotFoundException.class, () -> {
            vueloService.obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha);
        });
    }

    @Test
    @DisplayName("US5- Camino: no hay reservas realizadas para tal fecha.")
    void obtenerNumeroVentasIngresosDiariosThrowSecondEntityNotFoundExceptionTest() {

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
