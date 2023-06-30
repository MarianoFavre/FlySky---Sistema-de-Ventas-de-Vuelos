package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;

import java.util.List;

public interface IVueloService {
    List<VueloDto> obtenerVuelosDisponibles();

    ReservaVueloDto reservarVuelo(String nombreUsuario, int telefono, ReservaVueloDto reservaVueloDto) ;
}
