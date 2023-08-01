package com.codoacodo.flysky.demo.repository;

import com.codoacodo.flysky.demo.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long> {

     // No utilizada ya que fue obtenido de forma más eficiente llamado a un solo repositorio.
     //List<ReservaEntity> findByUsuario(UsuarioEntity usuarioEntity);

}
