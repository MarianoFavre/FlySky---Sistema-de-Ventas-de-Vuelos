package com.codoacodo.flysky.demo.dto.response;

import com.codoacodo.flysky.demo.model.entity.VueloEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter //El DTO es inmutable pero utilizamos el set porque lo necesita el objeto de tipo ModelMapper.
public class ButacaDto {

    private Boolean disponible;
    private String posicion;
}
