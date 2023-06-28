package com.codoacodo.flysky.demo.service;

import com.codoacodo.flysky.demo.dto.request.ReservaVueloDto;
import com.codoacodo.flysky.demo.model.entity.UsuarioEntity;
import com.codoacodo.flysky.demo.repository.IReservaRepository;
import com.codoacodo.flysky.demo.repository.IUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservaServiceImpl implements IReservaService {

    private IReservaRepository reservaRepository;
    private IUsuarioRepository usuarioRepository;

    public ReservaServiceImpl(IReservaRepository reservaRepository, IUsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /*
    public ReservaServiceImpl(IReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }
    */


    @Override
    public void reservarVuelo(String nombreUsuario, int telefono, ReservaVueloDto reservaVueloDto) {

        Optional<UsuarioEntity> usuario = usuarioRepository.findByNombreUsuarioAndTelefono(nombreUsuario, telefono);

        if (usuario.isPresent()) {
            if (usuario.get().getTipoUsuario().equals("CLIENTE")) {
                return;

            }

        }
        //lanzar una excepcion que diga el usuario no esta registrado, registrase como usuario antes de hacer reservas.


    }
}
