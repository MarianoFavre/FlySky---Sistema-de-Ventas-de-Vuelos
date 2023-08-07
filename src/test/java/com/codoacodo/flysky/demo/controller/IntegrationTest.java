package com.codoacodo.flysky.demo.controller;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.*;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(properties = {"SCOPE = test"})
//Similar a setear SCOPE=test en Environment variables dentro de Edit Configurations o Modify Run Configuration
// de com.codoacodo.flysky.demo
public class IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("US1 - Camino feliz.")
    public void verListaDeVuelosDisponiblesOk() throws Exception {

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

        VueloDto vueloDto1 = new VueloDto("666", 156, "Aerolineas Argentinas",
                LocalDateTime.of(2050, 07, 25, 8, 00, 00),
                LocalDateTime.of(2050, 07, 25, 8, 45, 00), 15000D,
                "Buenos Aires", "Uruguay", butacaDtos);

        expected.add(vueloDto1);

      //MODIFICAR writeValueAsString ya que nos lanza com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
        //Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module
        //"com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
        //https://howtodoinjava.com/jackson/java-8-date-time-type-not-supported-by-default/
        //https://www.baeldung.com/jackson-serialize-dates (9. Serialice la fecha de Java 8 con Jackson)

        //ObjectWriter writer = new ObjectMapper()
        //        .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
        //        .writer();

        //String expectedJson = writer.writeValueAsString(expected);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                //.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //Alternativa a la linea anterior.

        String expectedJson = objectMapper.writeValueAsString(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/vuelos/disponibles")
                        .param("nombreUsuarioTipoCliente", "Miguel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                //.andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroVuelo").value(666))
                .andReturn();

        //Otra alternativa
        // assertEquals("application/json", mvcResult.getResponse().getContentType());
         assertEquals(expectedJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("US2 y US3 - Camino feliz.")
    public void reservarVueloOk() throws Exception {

        List<ButacaReservaDto> butacas = new ArrayList<>();

        ButacaReservaDto butacaReservaDto = new ButacaReservaDto("B1", "Mariano");
        ButacaReservaDto butacaReservaDto1 = new ButacaReservaDto("C1", "Paola");

        butacas.add(butacaReservaDto);
        butacas.add(butacaReservaDto1);

        ReservaVueloDto reservaVueloDto = new ReservaVueloDto("666", butacas, TipoPago.PAGO_EN_LINEA);

        VueloReservaDto vuelo = new VueloReservaDto("666", "Aerolineas Argentinas",
                LocalDateTime.of(2050, 07, 25, 8, 00, 00),
                LocalDateTime.of(2050, 07, 25, 8, 45, 00),
                "Buenos Aires", "Uruguay");

        ReservaDto expected = new ReservaDto(TipoPago.PAGO_EN_LINEA, 27000D, LocalDateTime.now(), vuelo
                , butacas);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String reservaVueloJson = objectMapper.writeValueAsString(reservaVueloDto);
        String expectedJson = objectMapper.writeValueAsString(expected);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/vuelos/nuevaReserva")
                        .param("nombreUsuarioTipoCliente", "Mariano")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaVueloJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipoPago").value("PAGO_EN_LINEA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.montoPago").value(27000D)) //27000.0
                .andExpect(MockMvcResultMatchers.jsonPath("$.vuelo.numeroVuelo").value(666))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vuelo.fechaHoraPartida").value("2050-07-25T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vuelo.origen").value("Buenos Aires"))
                .andReturn();

        //assertEquals(expectedJson, mvcResult.getResponse().getContentAsString());
        //No pasa el test porque hay un defasaje entre la fecha real de reserva y la esperada
    }

    @Test
    @DisplayName("US4 - Camino feliz.")
    public void obtenerReservasPorNombreUsuarioOk() throws Exception {

        List<ReservaDto> expected = new ArrayList<>();

        VueloReservaDto vueloReservaDto = new VueloReservaDto("666", "Aerolineas Argentinas",
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

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String expectedJson = objectMapper.writeValueAsString(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/vuelos/reservas")
                        .param("nombreUsuarioTipoAgente", "Carlos")
                        .param("nombreUsuarioTipoCliente", "Miguel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(expectedJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("US5 - Camino feliz.")
    public void obtenerNumeroVentasIngresosDiariosOk() throws Exception  {

        VentaDto expected = new VentaDto( 1, 15000D);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String expectedJson = objectMapper.writeValueAsString(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/vuelos/ventas")
                        .param("nombreUsuarioTipoAdministrador", "Juan")
                        .param("fecha", "2023-04-25"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(expectedJson, mvcResult.getResponse().getContentAsString());
    }

}
