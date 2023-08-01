package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.dto.response.ReservaDto;
import com.codoacodo.flysky.demo.dto.response.VentaDto;
import com.codoacodo.flysky.demo.dto.response.VueloDto;

import java.time.LocalDate;
import java.util.List;

public interface IVueloService {
    List<VueloDto> obtenerVuelosDisponibles(String nombreUsuarioTipoCliente);

    ReservaDto reservarVuelo(String nombreUsuarioTipoCliente, ReservaVueloDto reservaVueloDto);

    List<ReservaDto> obtenerReservasPorNombreUsuario(String nombreUsuarioTipoAgente, String nombreUsuarioTipoCliente);

    VentaDto obtenerNumeroVentasIngresosDiarios(String nombreUsuarioTipoAdministrador, LocalDate fecha);

   // void borrarVuelos(Long id);
}
