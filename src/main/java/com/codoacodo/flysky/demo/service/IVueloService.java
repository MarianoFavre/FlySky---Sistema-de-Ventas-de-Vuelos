package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.response.VueloDto;

import java.util.List;

public interface IVueloService {
    List<VueloDto> obtenerVuelosDisponibles();
}
