package com.codoacodo.flysky.demo.controller;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.service.IVueloService;
import com.codoacodo.flysky.demo.service.VueloServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("api/v1/vuelos")
public class VueloController{
    private IVueloService vueloService;

    public VueloController(VueloServiceImpl vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping("disponibles/{nombreUsuarioTipoCliente}")
    public ResponseEntity<?> verListaDeVuelosDisponibles(@PathVariable String nombreUsuarioTipoCliente) {
        return new ResponseEntity<>(vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente), HttpStatus.OK);
    }

    @PutMapping("/nuevaReserva/{nombreUsuarioTipoCliente}")
    public ResponseEntity<?> reservarVuelo(@PathVariable String nombreUsuarioTipoCliente,
                                           @RequestBody ReservaVueloDto reservaVueloDto) {
        return new ResponseEntity<>(vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto), HttpStatus.OK);
    }

    @GetMapping("/reservas/{nombreUsuarioTipoAgente}/{nombreUsuarioTipoCliente}")
    public ResponseEntity<?> obtenerReservasPorNombreUsuario(@PathVariable String nombreUsuarioTipoAgente,
                                                  @PathVariable String nombreUsuarioTipoCliente) {

        return new ResponseEntity<>(vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente,
                nombreUsuarioTipoCliente), HttpStatus.OK);
    }

    @GetMapping("/ventas/{nombreUsuarioTipoAdministrador}/{fecha}")
    public ResponseEntity<?> obtenerNumeroVentasIngresosDiarios(@PathVariable String nombreUsuarioTipoAdministrador,
                                                                @PathVariable LocalDate fecha) {

        return new ResponseEntity<>(vueloService
                .obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha), HttpStatus.OK);
    }


}
