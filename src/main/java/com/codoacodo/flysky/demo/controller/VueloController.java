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
    private final IVueloService vueloService;

    public VueloController(VueloServiceImpl vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping("disponibles")
    public ResponseEntity<?> verListaDeVuelosDisponibles(@RequestParam String nombreUsuarioTipoCliente) {
        return new ResponseEntity<>(vueloService.obtenerVuelosDisponibles(nombreUsuarioTipoCliente), HttpStatus.OK);
    }

    @PutMapping("/nuevaReserva")
    public ResponseEntity<?> reservarVuelo(@RequestParam String nombreUsuarioTipoCliente,
                                           @RequestBody ReservaVueloDto reservaVueloDto) {
        return new ResponseEntity<>(vueloService.reservarVuelo(nombreUsuarioTipoCliente, reservaVueloDto), HttpStatus.OK);
    }

    @GetMapping("/reservas")
    public ResponseEntity<?> obtenerReservasPorNombreUsuario(@RequestParam  String nombreUsuarioTipoAgente,
                                                             @RequestParam  String nombreUsuarioTipoCliente) {

        return new ResponseEntity<>(vueloService.obtenerReservasPorNombreUsuario(nombreUsuarioTipoAgente,
                nombreUsuarioTipoCliente), HttpStatus.OK);
    }

    @GetMapping("/ventas")
    public ResponseEntity<?> obtenerNumeroVentasIngresosDiarios(@RequestParam  String nombreUsuarioTipoAdministrador,
                                                                @RequestParam  LocalDate fecha) {

        return new ResponseEntity<>(vueloService
                .obtenerNumeroVentasIngresosDiarios(nombreUsuarioTipoAdministrador, fecha), HttpStatus.OK);
    }


}
