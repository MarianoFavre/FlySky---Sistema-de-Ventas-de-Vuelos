package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.ReservaVueloResponseDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;

import java.util.List;

public interface IVueloService {
    List<VueloDto> obtenerVuelosDisponibles(String nombreUsuarioTipoCliente);

    ReservaVueloResponseDto reservarVuelo(String nombreUsuarioTipoCliente, ReservaVueloDto reservaVueloDto);

    List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String nombreUsuarioTipoCliente);
}
