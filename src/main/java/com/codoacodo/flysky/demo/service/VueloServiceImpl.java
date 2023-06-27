package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.codoacodo.flysky.demo.repository.IVueloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VueloServiceImpl implements IVueloService {
    private IVueloRepository vueloRepository;

    public VueloServiceImpl(IVueloRepository vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    @Override
    public List<VueloEntity> obtenerVuelosDisponibles() {
        return vueloRepository.findByDisponibleTrue();
    }
}
