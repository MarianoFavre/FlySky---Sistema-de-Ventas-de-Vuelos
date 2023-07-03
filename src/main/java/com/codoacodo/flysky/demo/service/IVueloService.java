package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.ReservaVueloResponseDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;

import java.util.List;

public interface IVueloService {
    List<VueloDto> obtenerVuelosDisponibles();

    ReservaVueloResponseDto reservarVuelo(String nombreUsuario, ReservaVueloDto reservaVueloDto) ;
}
