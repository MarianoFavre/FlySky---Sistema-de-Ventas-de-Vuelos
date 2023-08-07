package com.codoacodo.flysky.demo.repository;

import com.codoacodo.flysky.demo.model.entity.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVueloRepository extends JpaRepository<Vuelo, Long> {

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    Optional<Vuelo> findByNumeroVuelo(String numeroVuelo);

}

