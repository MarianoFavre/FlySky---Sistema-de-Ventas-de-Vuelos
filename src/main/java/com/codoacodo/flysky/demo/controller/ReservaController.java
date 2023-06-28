package com.codoacodo.flysky.demo.controller;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.service.IReservaService;
import com.codoacodo.flysky.demo.service.ReservaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reserva")
public class ReservaController {

    private IReservaService reservaService;

    public ReservaController(ReservaServiceImpl reservaService) {
        this.reservaService = reservaService;
    }
/*
    @PutMapping("/{nombreUsuario}/{telefono}")
    public ResponseEntity<?> reservarVuelo(@PathVariable String nombreUsuario,
                                           @PathVariable int telefono,
                                           @RequestBody ReservaVueloDto reservaVueloDto) {
       return new ResponseEntity<>(reservaService. , HttpStatus.OK);
    }
*/
}
