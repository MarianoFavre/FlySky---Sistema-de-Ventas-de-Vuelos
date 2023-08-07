package com.codoacodo.flysky.demo.controller;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.service.IVueloService;
import com.codoacodo.flysky.demo.service.VueloServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/v1/vuelos")
@Validated //Requiere para las anotaciones (@NotBlank, etc) de la dependencia spring-boot-starter-validation
public class VueloController {

    IVueloService vueloService;

    public VueloController(VueloServiceImpl vueloService) {
        this.vueloService = vueloService;
    }

    //User_Story_1
    @GetMapping("/disponibles")
    public ResponseEntity<?> verListaDeVuelosDisponibles(
            @RequestParam @NotBlank(message = "Ingrese un nombre de usuario de tipo cliente.") String nombreUsuarioTipoCliente) {
        return new ResponseEntity<>(vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente), HttpStatus.OK);
    }

    //User_Story_2_And_User_Story_3
    @PostMapping("/nuevaReserva")
    public ResponseEntity<?> reservarVuelo(
            @RequestParam @NotBlank(message = "Ingrese un nombre de usuario de tipo cliente.")String nombreUsuarioTipoCliente,
            @Valid @RequestBody ReservaVueloDto reservaVueloDto) {
        return new ResponseEntity<>(vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto), HttpStatus.OK);
    }

    //User_Story_4
    @GetMapping("/reservas")
    public ResponseEntity<?> obtenerReservasPorNombreUsuario(
            @RequestParam @NotBlank(message = "Ingrese un nombre de usuario de tipo agente.") String nombreUsuarioTipoAgente,
            @RequestParam @NotBlank(message = "Ingrese un nombre de usuario de tipo cliente.") String nombreUsuarioTipoCliente) {
        return new ResponseEntity<>(vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente,
                nombreUsuarioTipoCliente), HttpStatus.OK);
    }

    //User_Story_5
    @GetMapping("/ventas")
    public ResponseEntity<?> obtenerNumeroVentasIngresosDiarios(
            @RequestParam @NotBlank(message = "Ingrese un nombre de usuario de tipo administrador.") String nombreUsuarioTipoAdministrador,
            @RequestParam @PastOrPresent (message = "La fecha del informe diario de ventas no debe ser superior a la fecha actual.") LocalDate fecha) {
        return new ResponseEntity<>(vueloService
                .obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha), HttpStatus.OK);
    }

    //-----------------------------------------------------------------------------------------------------------------
/*
    @DeleteMapping("/borrar/{id}")
    public String borrarVuelos(@PathVariable Long id) {
        vueloService.borrarVuelos(id);
        return "Borrado exitoso";
    }
*/
}
