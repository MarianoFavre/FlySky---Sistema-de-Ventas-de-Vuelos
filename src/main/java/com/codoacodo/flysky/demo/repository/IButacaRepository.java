package com.codoacodo.flysky.demo.repository;

import com.codoacodo.flysky.demo.model.entity.Butaca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IButacaRepository extends JpaRepository<Butaca, Long> {

    Optional<Butaca> findByPosicion(String posicion);

}
