package com.codoacodo.flysky.demo.controller;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.*;
import com.codoacodo.flysky.demo.model.enums.TipoPago;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("US1 - Camino feliz.")
    public void verListaDeVuelosDisponiblesOk() throws Exception {

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

/*      //MODIFICAR writeValueAsString ya que nos lanza com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
        //Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module
        //"com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
        //https://howtodoinjava.com/jackson/java-8-date-time-type-not-supported-by-default/
        //https://www.baeldung.com/jackson-serialize-dates (9. Serialice la fecha de Java 8 con Jackson)

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String expectedJson = writer.writeValueAsString(expected);
*/
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
                //.andExpect(MockMvcResultMatchers.jsonPath("$[1].numeroVuelo").value(125))
                .andReturn();

        //Otra alternativa
        // assertEquals("application/json", mvcResult.getResponse().getContentType());
         assertEquals(expectedJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("US2 y US3 - Camino feliz.")
    public void reservarVueloOk() throws Exception {

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

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String reservaVueloJson = objectMapper.writeValueAsString(reservaVueloDto);
        String expectedJson = objectMapper.writeValueAsString(expected);


        MvcResult mvcResult = mockMvc.perform(put("/api/v1/vuelos/nuevaReserva")
                        .param("nombreUsuarioTipoCliente", "Mariano")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaVueloJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(expectedJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("US4 - Camino feliz.")
    public void obtenerNumeroVentasIngresosDiariosOk() throws Exception {

        List<ReservaDto> expected = new ArrayList<>();

        UsuarioDto usuarioDto = new UsuarioDto("Miguel", 156453);

        VueloReservaDto vueloReservaDto = new VueloReservaDto(666, "Aerolineas Argentinas",
                LocalDateTime.of(2023, 06, 25, 23, 53, 30),
                LocalDateTime.of(2023, 06, 25, 23, 53, 30), "Buenos Aires",
                "Uruguay");

        ReservaDto reservaDto = new ReservaDto(TipoPago.TRANSFERENCIA_BANCARIA, 15000D,
                LocalDate.of(2023, 04, 25), usuarioDto, vueloReservaDto, "AE04");

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
    public void obtenerReservasPorNombreUsuarioOk() throws Exception {

        VentaDto expected = new VentaDto( LocalDate.of(2023, 04, 25), 1, 15000D);

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
