package com.codoacodo.flysky.demo.repository;

import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVueloRepository extends JpaRepository<VueloEntity, Long> {

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    List<VueloEntity> findByDisponibleTrue();

}

