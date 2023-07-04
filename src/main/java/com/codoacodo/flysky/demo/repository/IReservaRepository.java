package com.codoacodo.flysky.demo.repository;

import com.codoacodo.flysky.demo.model.entity.ReservaEntity;
import com.codoacodo.flysky.demo.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<ReservaEntity, Long> {
     List<ReservaEntity> findByUsuario(UsuarioEntity usuarioEntity);
     List<ReservaEntity> findByFechaReserva(LocalDate fecha);
}
