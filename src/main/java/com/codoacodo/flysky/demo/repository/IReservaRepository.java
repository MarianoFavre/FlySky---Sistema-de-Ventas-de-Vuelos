package com.codoacodo.flysky.demo.repository;

import com.codoacodo.flysky.demo.model.entity.ReservaEntity;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservaRepository extends JpaRepository<ReservaEntity, Long> {

    List findByUsuario(String nombreUsuarioTipoCliente);
}
