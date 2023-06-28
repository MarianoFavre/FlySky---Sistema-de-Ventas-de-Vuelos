package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface IReservaService {

    void reservarVuelo(String nombreUsuario, int telefono, ReservaVueloDto reservaVueloDto) ;

}
